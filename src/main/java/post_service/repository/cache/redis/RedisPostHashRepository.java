package post_service.repository.cache.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import post_service.dto.comment.ResponseCommentDto;
import post_service.dto.post.ResponsePostDto;
import post_service.repository.cache.CachePostRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Repository
@Slf4j
public class RedisPostHashRepository extends RedisHashRepository<String, Long, ResponsePostDto> implements CachePostRepository {
    private final ExecutorService commentPostInCache;
    private final ExecutorService likePostInCache;

    public RedisPostHashRepository(
            RedisTemplate<String, ResponsePostDto> postTemplate, ExecutorService commentPostInCache, ExecutorService likePostInCache) {
        super(postTemplate, "post");
        this.commentPostInCache = commentPostInCache;
        this.likePostInCache = likePostInCache;
    }

    @Override
    public ResponsePostDto save(long postId, ResponsePostDto post) {
        return super.save(postId, post);
    }

    @Async("saveAllPostsToCache")
    @Override
    public void saveAll(Map<Long, ResponsePostDto> posts) {
        super.saveAll(posts);
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
