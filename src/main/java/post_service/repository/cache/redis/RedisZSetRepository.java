package post_service.repository.cache.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;
import java.util.concurrent.ExecutorService;

@Slf4j
public class RedisZSetRepository<H, V> extends OptimisticOperation<V> {
    private final RedisTemplate<String, V> template;
    private final ZSetOperations<String, V> zSetOperations;
    private final String key;

    protected RedisZSetRepository(RedisTemplate<String, V> template, String key) {
        super(template);
        this.template = template;
        this.zSetOperations = template.opsForZSet();
        this.key = key + ":";
    }

    protected Boolean add(H hash, V val, double score) {
        return zSetOperations.add(key + hash, val, score);
    }

    //todo add handler
    protected void addOptimistic(H hash, V val, double score, ExecutorService pool) {
        addOptimistic(key + hash, pool, () -> add(hash, val, score));
    }

    protected Long indexOf(H hash, V val) {
        validateKey(hash);

        Long idx = zSetOperations.rank(key + hash, val);
        if (idx == null) {
            throw new IllegalArgumentException("value " + val + " in key" + key + hash + " not exists");
        }

        return idx;
    }

    protected Set<V> getRange(H hash, long start, long end) {
        validateKey(hash);
        return zSetOperations.reverseRange(key + hash, start, end);
    }

    protected boolean isLast(H hash, V val) {
        validateKey(hash);
        Set<V> last = zSetOperations.reverseRange("scores", -1, -1);

        return last != null && last.contains(val);
    }

    protected boolean isEmpty(H hash) {
        return !hasKey(hash) || 0 > zSetOperations.size(key + hash);
    }

    private void validateKey(H hash) {
        if (!hasKey(hash)) {
            throw new IllegalArgumentException("key " + key + hash + " not exists");
        }
    }

    private boolean hasKey(H hash) {
        return hash != null || template.hasKey(key + hash);
    }
}
