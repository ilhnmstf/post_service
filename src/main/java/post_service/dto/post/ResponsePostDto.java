package post_service.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import post_service.dto.comment.ResponseCommentDto;
import java.util.LinkedList;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class ResponsePostDto {
    private long id;
    private String content;
    private long authorId;
    private int likeCount;
    private LinkedList<ResponseCommentDto> comments;
    private String createdAt;
    private String updatedAt;

    public ResponsePostDto addCommentFirst(ResponseCommentDto comment) {
        comments.addFirst(comment);
        return this;
    }

    public ResponsePostDto like() {
        likeCount++;
        return this;
    }
}
