package post_service.service.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import post_service.client.UserServiceClientV1;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserServiceClientV1 userServiceClientV1;

    @Override
    public void validate(long userId) {
        if (!userServiceClientV1.isExists(userId)) {
            throw new EntityNotFoundException("User with id " + userId + " not exists");
        }
    }
}
