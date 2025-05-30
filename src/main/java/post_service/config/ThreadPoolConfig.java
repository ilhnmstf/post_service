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
    public ExecutorService savePostToCache() {
        log.info("Init thread pool for 'save post to cache' method");
        return Executors.newFixedThreadPool(redisProperties.getThreadPoolSizes().getPost().getSave());
    }

    @Bean
    public ExecutorService saveAllPostsToCache() {
        log.info("Init thread pool for 'save all post to cache' method");
        return Executors.newFixedThreadPool(redisProperties.getThreadPoolSizes().getPost().getSaveAll());
    }

    @Bean
    public ExecutorService commentPostInCache() {
        log.info("Init thread pool for 'add comment post in cache' method");
        return Executors.newFixedThreadPool(redisProperties.getThreadPoolSizes().getPost().getComment());
    }

    @Bean
    public ExecutorService likePostInCache() {
        log.info("Init thread pool for 'like post in cache' method");
        return Executors.newFixedThreadPool(redisProperties.getThreadPoolSizes().getPost().getLike());
    }
}
