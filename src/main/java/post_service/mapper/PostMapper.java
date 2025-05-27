package post_service.mapper;

import org.mapstruct.Mapper;
import post_service.dto.post.ResponsePostDto;
import post_service.entity.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {

    ResponsePostDto toDto(Post post);
}
