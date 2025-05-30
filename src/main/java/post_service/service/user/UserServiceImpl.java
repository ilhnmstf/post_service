package post_service.service.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import post_service.client.UserServiceClientV1;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserServiceClientV1 userServiceClientV1;

    @Override
    public void validate(long userId) {
        log.debug("Check existing user with id {}", userId);
        if (!userServiceClientV1.isExists(userId)) {
            throw new EntityNotFoundException("User with id " + userId + " not exists");
        }
    }
}
