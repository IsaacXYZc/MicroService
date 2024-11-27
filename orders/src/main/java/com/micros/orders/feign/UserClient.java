package com.micros.orders.feign;

import com.micros.orders.models.UserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8080/api/users", configuration = FeignConfig.class)
public interface UserClient {

    @GetMapping("/{id}")
    UserRequest getUserById(@PathVariable("id") Long id);
}