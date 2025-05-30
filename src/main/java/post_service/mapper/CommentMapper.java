package post_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import post_service.dto.comment.ResponseCommentDto;
import post_service.entity.Comment;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", uses = {LocalDateTimeMapper.class})
public interface CommentMapper {

    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "toString")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "toString")
    ResponseCommentDto toDto(Comment comment);

    @Named("link")
    default LinkedList<ResponseCommentDto> link(List<Comment> comments) {
        return comments == null || comments.isEmpty() ? new LinkedList<>()
                : comments.stream().map(this::toDto).collect(Collectors.toCollection(LinkedList::new));
    }
}
