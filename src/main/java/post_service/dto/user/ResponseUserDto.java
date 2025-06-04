package post_service.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseUserDto {
    private long id;
    private List<Long> followeeIds;
    private List<Long> followerIds;
}
