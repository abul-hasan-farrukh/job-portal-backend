package com.aimbitious.job.client;

import com.aimbitious.job.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "AIMBITIOUS-JOB-PORTAL-USER-SERVICE")
public interface UserClient {

    @GetMapping("/api/users/{userId}")
    UserResponse getUserById(
            @PathVariable Long userId
    );
}
