package com.aimbitious.job.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyLocationRequest {

    @NotBlank(message = "City is required")
    private String city;

    private String label;
    private String address;
    private String state;

    @NotBlank(message = "Country is required")
    private String country;

    private String zipCode;
    private Double latitude;
    private Double longitude;
    private Boolean isHeadquarter = false;
}
