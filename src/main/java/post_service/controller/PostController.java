package post_service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import post_service.dto.comment.CreateCommentDto;
import post_service.dto.post.CreatePostDto;
import post_service.dto.post.ResponsePostDto;

// TODO create doc
@Validated
public interface PostController {
    ResponseEntity<ResponsePostDto> create(@RequestBody @Valid CreatePostDto createPost);

    ResponseEntity<Boolean> addComment (
            @PathVariable @Min(value = 1, message = "should more than 0") long postId,
            @RequestBody @Valid CreateCommentDto createComment);

    ResponseEntity<Boolean> like(
            @PathVariable @Min(value = 1, message = "should more than 0") long postId,
            @RequestParam @Min(value = 1, message = "should more than 0") long authorId);

    ResponseEntity<ResponsePostDto> get(@PathVariable @Min(value = 1, message = "should more than 0") long postId);
}
