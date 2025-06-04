package post_service.repository.cache.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


@Slf4j
public class OptimisticOperation<V> {

    private final RedisTemplate<String, V> template;

    protected OptimisticOperation(RedisTemplate<String, V> template) {
        this.template = template;
    }

    protected CompletableFuture<Boolean> addOptimistic(String key, ExecutorService pool, Runnable doing) {
        return doOptimistic(key, pool, operations -> {
            operations.watch(key);
            log.debug("Start redis transaction");
            operations.multi();
            doing.run();
            return operations.exec();
        });
    }

    protected CompletableFuture<Boolean> updateOptimistic(
            String key, ExecutorService pool, Supplier<V> preparation, Consumer<V> doing) {
        return doOptimistic(key, pool, operations -> {
            operations.watch(key);
            V updatedVal = preparation.get();
            log.info("Updated value {}", updatedVal);

            log.debug("Start redis transaction");
            operations.multi();
            doing.accept(updatedVal);
            return operations.exec();
        });
    }

    private CompletableFuture<Boolean> doOptimistic(
            String key, ExecutorService pool, Function<RedisOperations, Object> doing) {
        log.debug("Try to update value with key {}", key);
        return CompletableFuture.supplyAsync(() -> template.execute(new SessionCallback<>() {
            @Override
            public Boolean execute(RedisOperations operations) throws DataAccessException {
                log.debug("Monitoring the changes in the value in key {}", key);
                Object res = doing.apply(operations);
                log.debug("Finish redis transaction");

                return res != null;
            }
        }), pool);
    }
}
