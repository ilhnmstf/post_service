package post_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import post_service.dto.post.ResponsePostDto;
import post_service.service.NewsFeedService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/vi/feed")
public class NewsFeedControllerImpl implements NewsFeedController {
    private final NewsFeedService newsFeedService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<ResponsePostDto>> getFeed(@PathVariable long userId, @RequestParam long lastViewPostId) {
        return ResponseEntity.ok().body(newsFeedService.getNext(userId, lastViewPostId));
    }
}
