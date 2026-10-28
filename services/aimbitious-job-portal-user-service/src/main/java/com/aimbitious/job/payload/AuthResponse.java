package com.aimbitious.job.payload;

import com.aimbitious.job.dto.response.UserResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {

    private String jwt;
    private String title;
    private String message;
    private UserResponse user;
}
