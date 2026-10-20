package com.aimbitious.job.payload;

import com.aimbitious.job.domain.ResumeTemplate;
import com.aimbitious.job.domain.ResumeVisibility;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateResumeRequest {

    @NotBlank(message = "Resume title is required")
    private String title;

    private ResumeTemplate template;

    private ResumeVisibility visibility;

    private Boolean isDefault;
}
