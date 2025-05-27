package post_service.dto.like;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateLikeDto {

    @Min(value = 1, message = "should more than 0")
    private long postId;

    @Min(value = 1, message = "should more than 0")
    private long authorId;
}
