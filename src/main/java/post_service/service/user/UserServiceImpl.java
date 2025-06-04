package post_service.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import post_service.client.UserServiceClientV1;
import post_service.dto.user.ResponseUserDto;
import post_service.repository.cache.CacheUserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final CacheUserRepository cacheUserRepository;
    private final UserServiceClientV1 userServiceClientV1;

    @Override
    public void validate(long userId) {
        log.debug("Check existing user with id {}", userId);
        get(userId);
    }

    @Override
    public List<Long> getFollowee(long userId) {
        return get(userId).getFolloweeIds();
    }


    private ResponseUserDto get(long userId) {
        return cacheUserRepository.get(userId)
                .orElseGet(() -> userServiceClientV1.get(userId));
    }
}
