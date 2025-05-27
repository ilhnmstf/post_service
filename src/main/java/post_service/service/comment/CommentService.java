package post_service.service.comment;

import post_service.dto.comment.CreateCommentDto;
import post_service.dto.comment.ResponseCommentDto;

public interface CommentService {
    ResponseCommentDto create(CreateCommentDto createComment);
}
