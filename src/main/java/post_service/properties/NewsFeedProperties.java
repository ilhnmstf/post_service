package post_service.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "news-feed")
@Component
@Getter
@Setter
public class NewsFeedProperties {
    private int countInCache;
    private int count;
}
