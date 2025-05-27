package post_service.service.like;

import post_service.dto.like.CreateLikeDto;
import post_service.dto.like.ResponseLikeDto;

public interface LikeService {
    ResponseLikeDto create(CreateLikeDto createLike);
}
