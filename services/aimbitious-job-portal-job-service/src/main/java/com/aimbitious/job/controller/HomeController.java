package com.aimbitious.job.controller;

import com.aimbitious.job.domain.UserRole;
import com.aimbitious.job.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public ResponseEntity<ApiResponse> HomeController() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Service for managing job postings, search and filtering. --- " + UserRole.ROLE_EMPLOYER);
        apiResponse.setSuccess(true);

        return ResponseEntity.ok(apiResponse);
    }
}
