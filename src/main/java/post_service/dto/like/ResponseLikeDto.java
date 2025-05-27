package post_service.dto.like;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseLikeDto {
    private long id;
    private long postId;
    private long authorId;
    private LocalDateTime createdAt;
}
