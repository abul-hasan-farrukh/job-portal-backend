package com.aimbitious.job.mapper;

import com.aimbitious.job.dto.JobTagResponse;
import com.aimbitious.job.model.JobTag;

public class JobTagMapper {

    public static JobTagResponse toTagResponse(JobTag jobTag) {
        return JobTagResponse.builder()
                .id(jobTag.getId())
                .name(jobTag.getName())
                .slug(jobTag.getSlug())
                .build();
    }

}
