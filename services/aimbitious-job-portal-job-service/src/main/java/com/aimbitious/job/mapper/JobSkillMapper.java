package com.aimbitious.job.mapper;

import com.aimbitious.job.dto.response.JobSkillResponse;
import com.aimbitious.job.model.JobSkill;

public class JobSkillMapper {

    public static JobSkillResponse toJobSkillResponse(JobSkill skill) {

        return JobSkillResponse.builder()
                .id(skill.getId())
                .name(skill.getName())
                .slug(skill.getSlug())
                .category(skill.getCategory())
                .active(skill.getActive())
                .build();
    }
}
