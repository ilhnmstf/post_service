package post_service.service.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import post_service.client.UserServiceClientV1;
import post_service.repository.cache.CacheUserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final CacheUserRepository cacheUserRepository;
    private final UserServiceClientV1 userServiceClientV1;

    @Override
    public void validate(long userId) {
        log.debug("Check existing user with id {}", userId);
        if (!cacheUserRepository.isExists(userId)) {
            userServiceClientV1.get(userId)
                    .ifPresentOrElse(
                            user -> cacheUserRepository.save(userId, user),
                            () -> {throw new EntityNotFoundException("User with id " + userId + " not exists");});
        }
    }
}
