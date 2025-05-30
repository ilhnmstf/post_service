package post_service.service.post;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import post_service.dto.comment.CreateCommentDto;
import post_service.dto.post.CreatePostDto;
import post_service.dto.post.ResponsePostDto;
import post_service.entity.Post;
import post_service.mapper.CommentMapper;
import post_service.mapper.PostMapper;
import post_service.repository.cache.CachePostRepository;
import post_service.repository.db.PostRepository;
import post_service.service.comment.CommentService;
import post_service.service.like.LikeService;
import post_service.service.user.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final UserService userService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final PostRepository postRepository;
    private final CachePostRepository cachePostRepository;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    @Override
    public ResponsePostDto create(CreatePostDto createPost) {
        log.debug("Try to create post by {}", createPost);
        userService.validate(createPost.getAuthorId());
        ResponsePostDto post = postMapper.toDto(postRepository.save(new Post()
                .setContent(createPost.getContent())
                .setAuthorId(createPost.getAuthorId())
                .setContent(createPost.getContent())));
        log.info("Success create post in db and convert to response {}", post);

        cachePostRepository.save(post);
        return post;
    }

    @Override
    @Transactional
    public boolean addComment(long postId, CreateCommentDto createComment) {
        log.debug("Try to add comment {} to post with id {}", createComment, postId);
        userService.validate(createComment.getAuthorId());
        Post post = findById(postId);
        post.addComment(commentService.create(createComment, post));
        log.debug("update post {}", post);
        postRepository.save(post);
        log.info("Success add comment to post in db");
        return cachePostRepository.saveComment(post.getId(), commentMapper.toDto(post.getComments().get(0))).join();
    }

    @Override
    public boolean like(long postId, long authorId) {
        log.debug("User with id {} try to like post with id {}", authorId, postId);
        userService.validate(authorId);
        Post post = findById(postId);
        post.like(likeService.create(authorId, post));
        log.debug("update post {}", post);
        postRepository.save(post);
        log.info("Success add comment to post in db");
        return cachePostRepository.like(post.getId()).join();
    }

    @Override
    public ResponsePostDto get(long postId) {
        return cachePostRepository.get(postId)
                .orElseGet(() -> postMapper.toDto(findById(postId)));
    }

    private Post findById(long postId) {
        log.debug("Try to find post by id {}", postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post with id " + postId + " not exists"));
        log.debug("find post {}", post);
        return post;
    }
}
