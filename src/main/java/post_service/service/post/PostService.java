package post_service.service.post;

import post_service.dto.comment.CreateCommentDto;
import post_service.dto.post.CreatePostDto;
import post_service.dto.post.ResponsePostDto;

import java.util.List;

public interface PostService {

    ResponsePostDto create(CreatePostDto createPost);

    boolean addComment(long postId, CreateCommentDto createComment);

    boolean like(long postId, long authorId);

    ResponsePostDto get(long postId);

    List<ResponsePostDto> getByIds(List<Long> postIds);

    List<ResponsePostDto> getOverCachePosts(long userId, int countInCache, int count);

    void fillNewsFeedAndGet(long userId, int count);

}
