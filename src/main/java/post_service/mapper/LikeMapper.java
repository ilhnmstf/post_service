package post_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import post_service.dto.like.ResponseLikeDto;
import post_service.entity.Like;

@Mapper(componentModel = "spring")
public interface LikeMapper {

    @Mapping(target = "postId", source = "post.id")
    ResponseLikeDto toDto(Like like);
}
