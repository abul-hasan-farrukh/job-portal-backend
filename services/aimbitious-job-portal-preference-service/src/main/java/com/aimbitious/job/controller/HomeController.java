package com.aimbitious.job.controller;

import com.aimbitious.job.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public ResponseEntity<ApiResponse> HomeController() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Service for managing job preferences like saved job.");
        apiResponse.setSuccess(true);

        return ResponseEntity.ok(apiResponse);
    }
}
