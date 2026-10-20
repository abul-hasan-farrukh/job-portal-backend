package com.aimbitious.job.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class SavedJobResponse {

    private Long id;
    private Long candidateId;
    private Long jobId;
    private LocalDateTime savedAt;

}
