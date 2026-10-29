package com.aimbitious.job.dto.response;

import com.aimbitious.job.domain.LanguageProficiency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LanguageResponse {

    private Long id;

    private String languageName;

    private LanguageProficiency proficiency;

    private Integer displayOrder;
}
