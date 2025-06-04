package post_service.repository.cache.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import post_service.dto.comment.ResponseCommentDto;
import post_service.dto.post.ResponsePostDto;
import post_service.repository.cache.CachePostRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class RedisPostHashRepository extends RedisHashRepository<Long, ResponsePostDto> implements CachePostRepository {
    private final ExecutorService commentPostInCache;
    private final ExecutorService likePostInCache;
    private final ExecutorService saveAllPostsToCache;

    public RedisPostHashRepository(
            RedisTemplate<String, ResponsePostDto> postTemplate, ExecutorService commentPostInCache,
            ExecutorService likePostInCache, ExecutorService saveAllPostsToCache) {
        super(postTemplate, "post");
        this.commentPostInCache = commentPostInCache;
        this.likePostInCache = likePostInCache;
        this.saveAllPostsToCache = saveAllPostsToCache;
    }

    @Override
    public ResponsePostDto save(long postId, ResponsePostDto post) {
        return super.save(postId, post);
    }

    @Override
    public List<ResponsePostDto> saveAll(List<ResponsePostDto> posts) {
        CompletableFuture.runAsync(() ->
                saveAll(posts.stream()
                        .collect(Collectors.toMap(ResponsePostDto::getId, post -> post))), saveAllPostsToCache);
        return posts;
    }

    @Override
    public CompletableFuture<Boolean> saveCommentOptimistic(long postId, ResponseCommentDto comment) {
        log.debug("Try to save comment '{}' to post with id {} in redis", comment, postId);
        return super.updateOptimistic(postId, post -> post.addCommentFirst(comment), commentPostInCache);
    }

    @Override
    public CompletableFuture<Boolean> likeOptimistic(long postId) {
        log.debug("Try to like post with id {} in redis", postId);
        return super.updateOptimistic(postId, ResponsePostDto::like, likePostInCache);
    }

    @Override
    public Optional<ResponsePostDto> get(long postId) {
        return super.get(postId);
    }

    @Override
    public List<ResponsePostDto> getAll(List<Long> postIds) {
        return super.getAll(postIds);
    }
}
