package post_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import post_service.dto.post.ResponsePostDto;
import post_service.entity.Like;
import post_service.entity.Post;

import java.util.List;


@Mapper(componentModel = "spring", uses = {CommentMapper.class, LocalDateTimeMapper.class})
public interface PostMapper {

    @Mapping(target = "likeCount", source = "likes", qualifiedByName = "getLikesCount")
    @Mapping(target = "comments", source = "comments", qualifiedByName = "link")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "toString")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "toString")
    ResponsePostDto toDto(Post post);

    @Named("getLikesCount")
    default int getLikesCount(List<Like>likes) {
        return likes == null ? 0 : likes.size();
    }
}

