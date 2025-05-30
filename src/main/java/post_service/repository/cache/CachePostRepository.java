package post_service.repository.cache;

import post_service.dto.comment.ResponseCommentDto;
import post_service.dto.post.ResponsePostDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface CachePostRepository {
    ResponsePostDto save(long postId, ResponsePostDto post);

    void saveAll(Map<Long, ResponsePostDto> posts);

    CompletableFuture<Boolean> saveCommentOptimistic(long postId, ResponseCommentDto comment);

    CompletableFuture<Boolean> likeOptimistic(long postId);

    Optional<ResponsePostDto> get(long postId);

    List<ResponsePostDto> getAll(List<Long> postIds);
}
