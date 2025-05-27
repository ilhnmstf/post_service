package post_service.controller.post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import post_service.dto.post.CreatePostDto;
import post_service.dto.post.ResponsePostDto;
import post_service.service.post.PostService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/posts")
public class PostControllerImpl implements PostController {
    private final PostService postService;

    @PostMapping
    @Override
    public ResponseEntity<ResponsePostDto> create(CreatePostDto createPost) {
        return ResponseEntity.ok().body(postService.create(createPost));
    }
}
