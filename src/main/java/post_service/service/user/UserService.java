package post_service.service.user;

import java.util.List;

public interface UserService {
    void validate(long userId);

    List<Long> getFollowee(long userId);
}
