package post_service.repository.cache;

import post_service.dto.post.ResponsePostDto;

import java.util.List;

public interface CacheNewsFeedRepository {

    void addOptimistic(long userId, ResponsePostDto post);

    List<Long> getNext(long userId, long lastViewPostId, int countPosts);

    boolean isLast(long userId, long lastViewPostId);

    boolean isEmpty(long userId);
}
