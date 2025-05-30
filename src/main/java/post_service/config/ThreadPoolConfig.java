package post_service.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import post_service.properties.RedisProperties;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ThreadPoolConfig {
    private final RedisProperties redisProperties;

    @Bean
    public ExecutorService saveAllPostsToCache() {
        loggingInit("save all post to cache");
        return Executors.newFixedThreadPool(redisProperties.getThreadPoolSizes().getPost().getSaveAll());
    }

    @Bean
    public ExecutorService commentPostInCache() {
        loggingInit("save all post to cache");
        return Executors.newFixedThreadPool(redisProperties.getThreadPoolSizes().getPost().getComment());
    }

    @Bean
    public ExecutorService likePostInCache() {
        loggingInit("like post in cache");
        return Executors.newFixedThreadPool(redisProperties.getThreadPoolSizes().getPost().getLike());
    }

    @Bean
    public ExecutorService saveUserToCache() {
        loggingInit("save user to cache");
        return Executors.newFixedThreadPool(redisProperties.getThreadPoolSizes().getUser().getSave());
    }

    private void loggingInit(String message) {
        log.info("Init thread pool for '{}' method", message);
    }
}
