package post_service.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseCommentDto implements Serializable {
    private long id;
    private String content;
    private long authorId;
    private long postId;
    private String createdAt;
    private String updatedAt;
}
