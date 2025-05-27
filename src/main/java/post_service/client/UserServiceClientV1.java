package post_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}/api/v1/users")
public interface UserServiceClientV1 {

    @GetMapping("/{userId}/exists")
    boolean isExists(@PathVariable long userId);
}
