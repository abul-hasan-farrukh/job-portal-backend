package com.aimbitious.job.payload;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String fullName;

    private String email;

    private String profileImage;

    public String getPhone() {
        return null;
    }
}
