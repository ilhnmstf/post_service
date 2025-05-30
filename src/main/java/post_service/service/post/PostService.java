package post_service.service.post;

import post_service.dto.comment.CreateCommentDto;
import post_service.dto.post.CreatePostDto;
import post_service.dto.post.ResponsePostDto;

public interface PostService {

    ResponsePostDto create(CreatePostDto createPost);

    boolean addComment(long postId, CreateCommentDto createComment);

    boolean like(long postId, long authorId);

    ResponsePostDto get(long postId);
}
