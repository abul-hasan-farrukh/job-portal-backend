package com.aimbitious.job.payload;

import com.aimbitious.job.domain.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleLoginRequest {

    @NotBlank(message = "Google ID token is required")
    private String idToken;

    // Optional: used only when registering a new Google account
    private UserRole role;
}