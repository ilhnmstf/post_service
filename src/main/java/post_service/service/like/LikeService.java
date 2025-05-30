package post_service.service.like;

import post_service.entity.Like;
import post_service.entity.Post;

public interface LikeService {
    Like create(long authorId, Post post);
}
