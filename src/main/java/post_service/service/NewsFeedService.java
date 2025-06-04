package post_service.service;

import post_service.dto.post.ResponsePostDto;

import java.util.List;

public interface NewsFeedService {

    List<ResponsePostDto> getNext(long userId, long lastViewPostId);
}
