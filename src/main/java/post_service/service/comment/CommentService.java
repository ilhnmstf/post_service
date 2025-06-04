package post_service.service.comment;

import post_service.dto.comment.CreateCommentDto;
import post_service.dto.comment.ResponseCommentDto;
import post_service.entity.Post;

public interface CommentService {
    ResponseCommentDto create(CreateCommentDto createComment, Post post);
}
