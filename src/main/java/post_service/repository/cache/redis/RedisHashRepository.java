package post_service.repository.cache.redis;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static java.lang.String.*;

@Slf4j
public class RedisHashRepository<K, H, V>  {
    private final RedisTemplate<K, V> template;
    private final HashOperations<K, H, V> postOperations;
    private final K topicName;

    protected RedisHashRepository(RedisTemplate<K, V> template, K topicName) {
        this.template = template;
        this.topicName = topicName;
        this.postOperations = template.opsForHash();
    }

    protected V save(H hash,V val) {
        log.debug("Try to save value '{}' to key {}:{} to redis", val, topicName, hash);
        postOperations.put(topicName, hash, val);
        return val;
    }

    protected void saveAll(Map<H, V> vals) {
        log.debug("Try to save all values to redis");
        postOperations.putAll(topicName, vals);
    }

    protected Optional<V> get(H hash) {
        log.debug("Try to get value in key {}:{} in redis", topicName, hash);
        return Optional.ofNullable(postOperations.get(topicName, hash));
    }

    protected List<V> getAll(List<H> hashes) {
        log.debug("Try to get all by keys in redis");
        List<V> values = postOperations.multiGet(topicName, hashes);
        return values.contains(null) ? List.of() : values;
    }

    @Retryable(
            retryFor = {OptimisticLockingFailureException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 100, multiplier = 2)
    )
    protected CompletableFuture<Boolean> updateOptimistic(H hash, Function<V, V> valUpdater, ExecutorService pool) {
        log.debug("Try to update value with key {}:{}", topicName, hash);
        return CompletableFuture.supplyAsync(() -> template.execute(new SessionCallback<>() {
            @Override
            public Boolean execute(RedisOperations operations) throws DataAccessException {
                HashOperations<K, H, V> ops = operations.opsForHash();
                log.debug("Monitoring the changes in the value in key {}:{}", topicName, hash);
                operations.watch(topicName + ":" + hash);

                V updatedVal = valUpdater.apply(get(hash).orElseThrow(() ->
                        new EntityNotFoundException(format("value in key %s:%s not exists in redis", topicName, hash))));
                log.debug("Updated value {}", updatedVal);

                log.debug("Start redis transaction");
                operations.multi();
                ops.put(topicName, hash, updatedVal);
                Object res = operations.exec();
                log.debug("Successes save updated post to redis");

                return res != null;
            }
        }), pool);
    }

    protected boolean isExists(H hash) {
        log.info("Check user in key {}:{} in redis", topicName, hash);
        return postOperations.hasKey(topicName, hash);
    }
}
