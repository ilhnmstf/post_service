package post_service.controller.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import post_service.dto.comment.CreateCommentDto;
import post_service.dto.comment.ResponseCommentDto;
import post_service.service.comment.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/comments")
public class CommentControllerImpl implements CommentController {
    private final CommentService commentService;

    @PostMapping
    @Override
    public ResponseEntity<ResponseCommentDto> create(CreateCommentDto createComment) {
        return ResponseEntity.ok().body(commentService.create(createComment));
    }
}
