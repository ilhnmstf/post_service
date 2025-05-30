package post_service.repository.cache;

import post_service.dto.user.ResponseUserDto;

import java.util.Optional;

public interface CacheUserRepository {

    void save(long userId, ResponseUserDto user);

    Optional<ResponseUserDto> get(long userId);

    boolean isExists(long userId);
}
