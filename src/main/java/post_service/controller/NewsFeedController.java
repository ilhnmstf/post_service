package post_service.controller;

import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import post_service.dto.post.ResponsePostDto;

import java.util.List;

// todo create doc
public interface NewsFeedController {

    ResponseEntity<List<ResponsePostDto>> getFeed(
            @PathVariable @Min(value = 1, message = "should be more 0") long userId,
            @RequestParam long lastViewPostId);
}
