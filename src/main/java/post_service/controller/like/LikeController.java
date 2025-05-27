package post_service.controller.like;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import post_service.dto.like.CreateLikeDto;
import post_service.dto.like.ResponseLikeDto;

// TODO create doc
public interface LikeController {
    ResponseEntity<ResponseLikeDto> create(@RequestBody @Valid CreateLikeDto createLike);
}
