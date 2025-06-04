package post_service.service.post;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import post_service.adder.PostAdder;
import post_service.dto.comment.CreateCommentDto;
import post_service.dto.post.CreatePostDto;
import post_service.dto.post.ResponsePostDto;
import post_service.entity.Post;
import post_service.mapper.PostMapper;
import post_service.repository.cache.CachePostRepository;
import post_service.repository.db.PostRepository;
import post_service.service.comment.CommentService;
import post_service.service.like.LikeService;
import post_service.service.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final UserService userService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final PostAdder postAdder;
    private final PostRepository postRepository;
    private final CachePostRepository cachePostRepository;
    private final PostMapper postMapper;

    @Override
    public ResponsePostDto create(CreatePostDto createPost) {
        log.debug("Try to create post by {}", createPost);
        userService.validate(createPost.getAuthorId());
        ResponsePostDto post = postMapper.toDto(postRepository.save(new Post()
                .setContent(createPost.getContent())
                .setAuthorId(createPost.getAuthorId())
                .setContent(createPost.getContent())));
        log.info("Success create post in db and convert to response {}", post);

        cachePostRepository.save(post.getId(), post);
        postAdder.addPost(createPost.getAuthorId(), post);

        return post;
    }

    @Override
    @Transactional
    public boolean addComment(long postId, CreateCommentDto createComment) {
        log.debug("Try to add comment {} to post with id {}", createComment, postId);
        userService.validate(createComment.getAuthorId());
        return cachePostRepository.saveCommentOptimistic(
                postId, commentService.create(createComment, findById(postId))).join();
    }

    @Override
    public boolean like(long postId, long authorId) {
        log.debug("User with id {} try to like post with id {}", authorId, postId);
        userService.validate(authorId);
        likeService.create(authorId, findById(postId));
        cachePostRepository.likeOptimistic(postId);
        return true;
    }

    @Override
    public ResponsePostDto get(long postId) {
        return cachePostRepository.get(postId)
                .orElseGet(() -> cachePostRepository.save(postId, postMapper.toDto(findById(postId))));
    }

    @Override
    public List<ResponsePostDto> getByIds(List<Long> postIds) {
        List<ResponsePostDto> posts = cachePostRepository.getAll(postIds);

        return posts.contains(null) ?
                cachePostRepository.saveAll(postRepository.findAllById(postIds).stream().map(postMapper::toDto).toList())
                : posts;
    }

    @Override
    public List<ResponsePostDto> getOverCachePosts(long userId, int countInCache, int count) {
        List<ResponsePostDto> posts = getFolloweePostsWithLimit(userId, countInCache, count);

        if (posts.isEmpty()) {
            throw new RuntimeException("posts for user with id " + userId + " ended");
        }
        return posts;
    }

    @Override
    public void fillNewsFeedAndGet(long userId, int countInCache) {
        cachePostRepository.saveAll(getFolloweePostsWithLimit(userId, 0 , countInCache));
    }

    private List<ResponsePostDto> getFolloweePostsWithLimit(long userId, int start, int end) {
        return postRepository.getByFolloweeIds(userService.getFollowee(userId), PageRequest.of(start, end)).stream()
                .map(postMapper::toDto)
                .toList();
    }

    private Post findById(long postId) {
        log.debug("Try to find post by id {} in db", postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post with id " + postId + " not exists"));
        if (post.isDeleted()) {
            throw new IllegalArgumentException("Post with id " + postId + " has been deleted");
        }
        log.debug("find post {} in db", post);
        return post;
    }
}
