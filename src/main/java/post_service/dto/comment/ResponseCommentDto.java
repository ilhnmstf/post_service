package post_service.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseCommentDto {
    private long id;
    private String content;
    private long authorId;
    private long postId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
