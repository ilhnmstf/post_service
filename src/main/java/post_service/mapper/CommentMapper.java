package post_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import post_service.dto.comment.ResponseCommentDto;
import post_service.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "postId", source = "post.id")
    ResponseCommentDto toDto(Comment comment);
}
