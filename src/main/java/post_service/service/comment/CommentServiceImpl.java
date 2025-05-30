package post_service.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import post_service.dto.comment.CreateCommentDto;
import post_service.entity.Comment;
import post_service.entity.Post;
import post_service.repository.db.CommentRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public Comment create(CreateCommentDto createComment, Post post) {
        log.debug("try to create comment by {} to {}", createComment, post);
        return commentRepository.save(new Comment()
                .setContent(createComment.getContent())
                .setAuthorId(createComment.getAuthorId())
                .setPost(post));
    }
}
