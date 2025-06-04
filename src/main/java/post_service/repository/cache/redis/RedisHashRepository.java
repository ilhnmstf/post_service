package post_service.repository.cache.redis;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static java.lang.String.*;

@Slf4j
public class RedisHashRepository<H, V> extends OptimisticOperation<V> {
    private final HashOperations<String, H, V> postOperations;
    private final String key;
    private final String valueNotFoundMsg = "value in key %s:%s not exists in redis";

    protected RedisHashRepository(RedisTemplate<String, V> template, String key) {
        super(template);
        this.postOperations = template.opsForHash();
        this.key = key;
    }

    protected V save(H hash,V val) {
        CompletableFuture.runAsync(() -> {
            log.debug("Try to save value '{}' to key {}:{} to redis", val, key, hash);
            postOperations.put(key, hash, val);
        }); // todo add pool
        return val;
    }

    protected void saveAll(Map<H, V> vals) {
        log.debug("Try to save all values to redis");
        postOperations.putAll(key, vals);
    }

    protected Optional<V> get(H hash) {
        log.info("Try to get value in key {}:{} in redis", key, hash);
        V val = postOperations.get(key, hash);
        return Optional.ofNullable(val);
    }

    protected List<V> getAll(List<H> hashes) {
        log.debug("Try to get all by keys in redis");
        return postOperations.multiGet(key, hashes);
    }

    protected CompletableFuture<Boolean> updateOptimistic(H hash, Function<V, V> valUpdater, ExecutorService pool) {
        return updateOptimistic(
                key + ":" + hash, pool,
                () -> valUpdater.apply(get(hash)
                        .orElseThrow(() -> new EntityNotFoundException(format(valueNotFoundMsg, key, hash)))),
                updatedVal -> postOperations.put(key, hash, updatedVal));
    }
}
