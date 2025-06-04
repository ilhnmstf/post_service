package post_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import post_service.dto.comment.CreateCommentDto;
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
    public ResponseEntity<ResponsePostDto> create(@RequestBody CreatePostDto createPost) {
        return ResponseEntity.ok().body(postService.create(createPost));
    }

    @PostMapping("/{postId}/comment")
    @Override
    public ResponseEntity<Boolean> addComment (@PathVariable long postId, @RequestBody CreateCommentDto createComment) {
        return ResponseEntity.ok().body(postService.addComment(postId, createComment));
    }

    @PostMapping("/{postId}/like")
    @Override
    public ResponseEntity<Boolean> like(@PathVariable  long postId, @RequestParam  long authorId) {
        return ResponseEntity.ok().body(postService.like(postId, authorId));
    }

    @GetMapping("/{postId}")
    @Override
    public ResponseEntity<ResponsePostDto> get(@PathVariable long postId) {
        return ResponseEntity.ok().body(postService.get(postId));
    }
}
