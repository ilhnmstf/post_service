package post_service.repository.cache;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import post_service.dto.comment.ResponseCommentDto;
import post_service.dto.post.ResponsePostDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisPostRepository implements CachePostRepository {
    private final RedisTemplate<String, ResponsePostDto> postTemplate;
    private final ExecutorService commentPostInCache;
    private final ExecutorService likePostInCache;
    private final String topicName = "post";
    private HashOperations<String, Long, ResponsePostDto> postOperations;

    @PostConstruct
    private void init() {
        this.postOperations = postTemplate.opsForHash();
    }

    @Override
    @Async("savePostToCache")
    public void save(ResponsePostDto post) {
        log.debug("Try to save post '{}' to redis", post);
        postOperations.put(topicName, post.getId(), post);
    }

    @Async("saveAllPostsToCache")
    @Override
    public void saveAll(Map<Long, ResponsePostDto> posts) {
        log.debug("Try to save posts '{}' to redis", posts);
        postOperations.putAll(topicName, posts);
    }

    @Retryable(
            retryFor = {OptimisticLockingFailureException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 100, multiplier = 2)
    )
    @Override
    public CompletableFuture<Boolean> saveComment(long postId, ResponseCommentDto comment) {
        log.debug("Try to save comment '{}' to post with id {} in redis", comment, postId);
        return updatePostOptimistic(postId, post -> post.addCommentFirst(comment), commentPostInCache);
    }

    @Retryable(
            retryFor = {OptimisticLockingFailureException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 100, multiplier = 2)
    )
    @Override
    public CompletableFuture<Boolean> like(long postId) {
        log.debug("Try to like post with id {} in redis", postId);
        return updatePostOptimistic(postId, ResponsePostDto::like, likePostInCache);
    }

    @Override
    public Optional<ResponsePostDto> get(long postId) {
        log.debug("Try to get post with id '{}' in redis", postId);
        return Optional.ofNullable(postOperations.get(topicName, postId));
    }

    @Override
    public List<ResponsePostDto> getAll(List<Long> postIds) {
        log.debug("Try to get posts with id '{}' in redis", postIds);
        List<ResponsePostDto> posts = postOperations.multiGet(topicName, postIds);
        return posts.contains(null) ? List.of() : posts;
    }

    private CompletableFuture<Boolean> updatePostOptimistic(
            long postId, Function<ResponsePostDto, ResponsePostDto> updatePost, ExecutorService threadPool) {

        return CompletableFuture.supplyAsync(() -> postTemplate.execute(new SessionCallback<>() {
            @Override
            public Boolean execute(RedisOperations operations) throws DataAccessException {
                log.debug("Try to update and save post with id {}", postId);
                HashOperations<String, Long, ResponsePostDto> ops = operations.opsForHash();
                log.debug("Monitoring the changes in the post with id {}", postId);
                operations.watch(topicName + ":" + postId);

                ResponsePostDto post = get(postId)
                        .orElseThrow(() -> new EntityNotFoundException("Post with id " + postId + " not exists in redis"));
                log.debug("Get post {}", post);
                ResponsePostDto updatedPost = updatePost.apply(post);
                log.debug("Update post {}", updatedPost);

                log.debug("Start redis transaction");
                operations.multi();
                ops.put(topicName, postId, updatedPost);
                Object res = operations.exec();
                log.debug("Successes save updated post to redis");

                return res != null;
            }
        }), threadPool);
    }
}
