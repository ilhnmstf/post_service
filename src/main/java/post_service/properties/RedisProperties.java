package post_service.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@ConfigurationProperties(prefix = "spring.data.redis")
@Component
@Getter
@Setter
public class RedisProperties {
    private String host;
    private int port;
    private Duration ttl;
    private ThreadPoolSizes threadPoolSizes;

    @Getter
    @Setter
    public static class ThreadPoolSizes {
        private Post post;
    }

    @Getter
    @Setter
    public static class Post {
        private int save;
        private int saveAll;
        private int comment;
        private int like;
    }
}
