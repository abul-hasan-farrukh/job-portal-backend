package com.aimbitious.job.payload;

import com.aimbitious.job.dto.response.JobSkillResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BulkJobSkillResponse {

    private int totalRequested;
    private int totalSucceeded;
    private int totalFailed;

    /** Successfully created skills. */
    private List<JobSkillResponse> succeeded;

    /** Skills that failed, with their index and reason. */
    private List<BulkJobSkillFailure> failed;
}
