package post_service.service.post;

import post_service.dto.post.CreatePostDto;
import post_service.dto.post.ResponsePostDto;
import post_service.entity.Post;

public interface PostService {

    ResponsePostDto create(CreatePostDto createPost);

    Post findById(long postId);
}
