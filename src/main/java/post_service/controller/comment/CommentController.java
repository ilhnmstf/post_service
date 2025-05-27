package post_service.controller.comment;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import post_service.dto.comment.CreateCommentDto;
import post_service.dto.comment.ResponseCommentDto;
// TODO create doc
public interface CommentController {
    ResponseEntity<ResponseCommentDto> create(@RequestBody @Valid CreateCommentDto createComment);
}
