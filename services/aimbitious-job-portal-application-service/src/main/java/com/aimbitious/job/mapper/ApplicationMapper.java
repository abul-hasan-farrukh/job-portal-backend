package com.aimbitious.job.mapper;

import com.aimbitious.job.domain.ApplicationStatus;
import com.aimbitious.job.dto.ApplicationNoteResponse;
import com.aimbitious.job.dto.ApplicationResponse;
import com.aimbitious.job.dto.CompanyResponse;
import com.aimbitious.job.dto.JobResponse;
import com.aimbitious.job.dto.response.UserResponse;
import com.aimbitious.job.model.Application;
import com.aimbitious.job.model.ApplicationNote;
import com.aimbitious.job.payload.CreateApplicationRequest;

import java.util.List;

public class ApplicationMapper {

    public static Application toEntity(CreateApplicationRequest req,
                                       Long candidateId,
                                       Long companyId,
                                       Long employerId) {

        if (req == null) return null;

        return Application.builder()
                .candidateId(candidateId)
                .jobId(req.getJobId())
                .companyId(companyId)
                .employerId(employerId)
                .resumeId(req.getResumeId())
                .coverLetter(req.getCoverLetter())
                .expectedSalary(req.getExpectedSalary())
                .availableFrom(req.getAvailableFrom())
                .status(ApplicationStatus.PENDING)
                .build();

    }

    public static ApplicationResponse toResponse(Application application,
                                                 List<ApplicationNote> notes,
                                                 JobResponse job,
                                                 CompanyResponse company,
                                                 UserResponse candidate
//                                                 ApplicationScreening screening
    ) {

        return ApplicationResponse.builder()
                .id(application.getId())
                .candidate(candidate)
                .employerId(application.getEmployerId())
                .job(job)
                .company(company)
                .status(application.getStatus())

                .resumeId(application.getResumeId())
                .coverLetter(application.getCoverLetter())

                .expectedSalary(application.getExpectedSalary())

                .availableFrom(application.getAvailableFrom())

                .isStarred(application.getIsStarred())

              .notes(notes.stream().map(ApplicationMapper::toNoteResponse).toList())
                .withdrawnAt(application.getWithdrawnAt())
                .withdrawnReason(application.getWithdrawnReason())
                .appliedAt(application.getAppliedAt())
                .updatedAt(application.getUpdatedAt())

//              .screening(toScreeningResponse(screening))
                .build();
    }

    public static ApplicationNoteResponse toNoteResponse(ApplicationNote note) {

        return ApplicationNoteResponse.builder()
                .id(note.getId())
                .addedByUserId(note.getAddedByUserId())
                .content(note.getContent())
                .createdAt(note.getCreatedAt())
                .build();
    }
}
