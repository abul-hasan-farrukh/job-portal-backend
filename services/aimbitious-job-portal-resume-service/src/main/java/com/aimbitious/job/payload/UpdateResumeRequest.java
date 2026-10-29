package com.aimbitious.job.payload;

import com.aimbitious.job.domain.ResumeTemplate;
import com.aimbitious.job.domain.ResumeVisibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateResumeRequest {

    @NotBlank(message = "Resume title is required")
    @Size(max = 150, message = "Title must not exceed 150 characters")
    private String title;

    private ResumeTemplate template;

    private ResumeVisibility visibility;
}
