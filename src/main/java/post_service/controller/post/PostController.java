package post_service.controller.post;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import post_service.dto.post.CreatePostDto;
import post_service.dto.post.ResponsePostDto;
// TODO create doc

public interface PostController {
    ResponseEntity<ResponsePostDto> create(@RequestBody @Valid CreatePostDto createPost);
}
