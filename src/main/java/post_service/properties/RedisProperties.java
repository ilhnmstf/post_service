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
        private User user;
    }

    @Getter
    @Setter
    public static class Post {
        private int saveAll;
        private int comment;
        private int like;
    }

    @Getter
    @Setter
    public static class User {
        private int save;
    }
}
