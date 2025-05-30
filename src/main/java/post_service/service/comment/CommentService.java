package post_service.service.comment;

import post_service.dto.comment.CreateCommentDto;
import post_service.entity.Comment;
import post_service.entity.Post;

public interface CommentService {
    Comment create(CreateCommentDto createComment, Post post);
}
