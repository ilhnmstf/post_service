package post_service.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import post_service.dto.comment.CreateCommentDto;
import post_service.dto.comment.ResponseCommentDto;
import post_service.entity.Comment;
import post_service.mapper.CommentMapper;
import post_service.repository.db.CommentRepository;
import post_service.service.post.PostService;
import post_service.service.user.UserService;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserService userService;
    private final PostService postService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    @Override
    public ResponseCommentDto create(CreateCommentDto createComment) {
        userService.validate(createComment.getAuthorId());
        return commentMapper.toDto(
                commentRepository.save(new Comment()
                        .setContent(createComment.getContent())
                        .setAuthorId(createComment.getAuthorId())
                        .setPost(postService.findById(createComment.getPostId()))));
    }
}
