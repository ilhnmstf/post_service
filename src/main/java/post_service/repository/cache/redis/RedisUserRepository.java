package post_service.repository.cache.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import post_service.dto.user.ResponseUserDto;
import post_service.repository.cache.CacheUserRepository;

import java.util.Optional;

@Repository
public class RedisUserRepository extends RedisHashRepository<String, Long, ResponseUserDto> implements CacheUserRepository {

    public RedisUserRepository(RedisTemplate<String, ResponseUserDto> userTemplate) {
        super(userTemplate, "user");
    }

    @Async("saveUserToCache")
    @Override
    public void save(long userId, ResponseUserDto user) {
        super.save(userId, user);
    }

    @Override
    public Optional<ResponseUserDto> get(long userId) {
        return super.get(userId);
    }

    @Override
    public boolean isExists(long userId) {
        return super.isExists(userId);
    }
}
