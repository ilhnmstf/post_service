package post_service.adder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import post_service.dto.post.ResponsePostDto;
import post_service.repository.cache.CacheNewsFeedRepository;
import post_service.service.user.UserService;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class PostAdderWithoutKafka implements PostAdder {
    private final UserService userService;
    private final CacheNewsFeedRepository cacheNewsFeedRepository;

    @Override
    public void addPost(long userId, ResponsePostDto post) {
        CompletableFuture.runAsync(() ->
                userService.getFollowee(userId).forEach(followeeId ->
                        cacheNewsFeedRepository.addOptimistic(followeeId, post)));//todo add pool
    }
}
