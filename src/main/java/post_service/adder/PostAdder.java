package post_service.adder;

import post_service.dto.post.ResponsePostDto;

public interface PostAdder {

    void addPost(long userId, ResponsePostDto post);
}
