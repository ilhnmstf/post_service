package post_service.service.post;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import post_service.dto.post.CreatePostDto;
import post_service.dto.post.ResponsePostDto;
import post_service.entity.Post;
import post_service.mapper.PostMapper;
import post_service.repository.db.PostRepository;
import post_service.service.user.UserService;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final UserService userService;
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public ResponsePostDto create(CreatePostDto createPost) {
        userService.validate(createPost.getAuthorId());
        return postMapper.toDto(
                postRepository.save(new Post()
                        .setContent(createPost.getContent())
                        .setAuthorId(createPost.getAuthorId())));
    }

    @Override
    public Post findById(long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post with id " + postId + " not exists"));
    }
}
