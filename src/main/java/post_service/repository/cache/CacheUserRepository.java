package post_service.repository.cache;

import post_service.dto.user.ResponseUserDto;

import java.util.Optional;

public interface CacheUserRepository {

    ResponseUserDto save(long userId, ResponseUserDto user);

    Optional<ResponseUserDto> get(long userId);
}
