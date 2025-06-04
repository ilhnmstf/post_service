package post_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import post_service.dto.post.ResponsePostDto;
import post_service.properties.NewsFeedProperties;
import post_service.repository.cache.CacheNewsFeedRepository;
import post_service.service.post.PostService;
import post_service.service.user.UserService;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class NewsFeedServiceImpl implements NewsFeedService {

    private final PostService postService;
    private final UserService userService;
    private final CacheNewsFeedRepository cacheNewsFeedRepository;
    private final NewsFeedProperties newsFeedProperties;


    @Override
    public List<ResponsePostDto> getNext(long userId, long lastViewPostId) {
        userService.validate(userId);

        if (cacheNewsFeedRepository.isEmpty(userId)) {
            postService.fillNewsFeedAndGet(userId, newsFeedProperties.getCountInCache());
            sleep();
        }

        if (cacheNewsFeedRepository.isLast(userId, lastViewPostId)) {
            return postService.getOverCachePosts(userId, newsFeedProperties.getCountInCache(), newsFeedProperties.getCount());
        }

        return postService.getByIds(
                cacheNewsFeedRepository.getNext(userId, lastViewPostId, newsFeedProperties.getCount()));
    }


    private void sleep() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
