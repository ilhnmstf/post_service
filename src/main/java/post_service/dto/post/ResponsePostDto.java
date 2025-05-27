package post_service.dto.post;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponsePostDto {
    private long id;
    private String content;
    private long authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
