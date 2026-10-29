package com.aimbitious.job.dto.response;

import com.aimbitious.job.domain.ApplicationStatus;
import com.aimbitious.job.dto.response.UserResponse;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Full application detail — used on the application detail page (employer view).
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationResponse {

    private Long id;
    private UserResponse candidate;
    private Long employerId;

    private JobSummaryResponse job;
    private CompanySummaryResponse company;

    private ApplicationStatus status;

    // Submission content
    private Long resumeId;
    private String coverLetter;

    // Candidate preferences
    private BigDecimal expectedSalary;
    private LocalDate availableFrom;

    // Tracking
    private Boolean isRead;
    private Boolean isStarred;

    // Related data (populated on demand)
    private List<ApplicationStatusHistoryResponse> statusHistory;
    private List<InterviewResponse> interviews;
    private List<ApplicationNoteResponse> notes;

    // Withdrawal
    private LocalDateTime withdrawnAt;
    private String withdrawnReason;

    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;

    // AI screening result — null until background scoring completes
    private ApplicationScreeningResponse screening;
}
