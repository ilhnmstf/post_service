package post_service.service.like;

import post_service.entity.Post;

public interface LikeService {
    void create(long authorId, Post post);
}
