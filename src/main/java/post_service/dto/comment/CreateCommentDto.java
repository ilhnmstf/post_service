package post_service.dto.comment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCommentDto {
    @NotBlank(message = "should not be empty")
    private String content;

    @Min(value = 1, message = "should more than 0")
    private long authorId;
}
