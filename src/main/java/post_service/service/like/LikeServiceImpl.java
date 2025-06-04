package post_service.service.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import post_service.entity.Like;
import post_service.entity.Post;
import post_service.repository.db.LikeRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;

    @Override
    @Transactional
    public void create(long authorId, Post post) {
        log.debug("author with id {} like post {}", authorId, post);
        Like like = likeRepository.save(new Like()
                .setAuthorId(authorId)
                .setPost(post));
        log.info("like {} was create in db", like);
    }
}
