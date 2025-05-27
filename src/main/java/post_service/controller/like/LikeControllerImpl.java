package post_service.controller.like;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import post_service.dto.like.CreateLikeDto;
import post_service.dto.like.ResponseLikeDto;
import post_service.service.like.LikeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/likes")
public class LikeControllerImpl implements LikeController {
    private final LikeService likeService;

    @PostMapping
    @Override
    public ResponseEntity<ResponseLikeDto> create(CreateLikeDto createLike) {
        return ResponseEntity.ok().body(likeService.create(createLike));
    }
}
