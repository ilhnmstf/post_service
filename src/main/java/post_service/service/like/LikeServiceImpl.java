package post_service.service.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import post_service.dto.like.CreateLikeDto;
import post_service.dto.like.ResponseLikeDto;
import post_service.entity.Like;
import post_service.mapper.LikeMapper;
import post_service.repository.db.LikeRepository;
import post_service.service.post.PostService;
import post_service.service.user.UserService;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final UserService userService;
    private final PostService postService;
    private final LikeMapper likeMapper;
    private final LikeRepository likeRepository;

    @Override
    public ResponseLikeDto create(CreateLikeDto createLike) {
        userService.validate(createLike.getAuthorId());
        return likeMapper.toDto(
                likeRepository.save(new Like()
                        .setAuthorId(createLike.getAuthorId())
                        .setPost(postService.findById(createLike.getPostId()))));
    }
}
