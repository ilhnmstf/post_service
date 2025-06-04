package post_service.repository.cache.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import post_service.dto.post.ResponsePostDto;
import post_service.repository.cache.CacheNewsFeedRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Repository
public class RedisNewsFeedZSetRepository extends RedisZSetRepository<Long, Long> implements CacheNewsFeedRepository {
    private final ExecutorService addOptimisticPool = Executors.newFixedThreadPool(5);

    public RedisNewsFeedZSetRepository(RedisTemplate<String, Long> feedTemplate) {
        super(feedTemplate, "feed");
    }

    @Override
    public void addOptimistic(long userId, ResponsePostDto post) {
        super.addOptimistic(userId, post.getId(),getScore(post), addOptimisticPool);
    }

    @Override
    public List<Long> getNext(long userId, long lastViewPostId, int countPosts) {
        long start = lastViewPostId < 1 ? 0 : super.indexOf(userId, lastViewPostId);
        return new ArrayList<>(super.getRange(userId, start, start + countPosts));
    }

    @Override
    public boolean isLast(long userId, long lastViewPostId) {
        return super.isLast(userId, lastViewPostId);
    }

    @Override
    public boolean isEmpty(long userId) {
        return super.isEmpty(userId);
    }

    private double getScore(ResponsePostDto post) {
        return LocalDateTime.parse(post.getCreatedAt()).atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }
}
