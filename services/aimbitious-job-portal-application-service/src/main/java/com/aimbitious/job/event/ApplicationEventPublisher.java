package com.aimbitious.job.event;

import com.aimbitious.job.client.CompanyClient;
import com.aimbitious.job.client.JobClient;
import com.aimbitious.job.client.UserClient;
import com.aimbitious.job.domain.ApplicationStatus;
import com.aimbitious.job.dto.CompanyResponse;
import com.aimbitious.job.dto.JobResponse;
import com.aimbitious.job.dto.response.UserResponse;
import com.aimbitious.job.model.Application;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor

public class ApplicationEventPublisher {

    public static final String TOPIC = "application.status.changed";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final UserClient userClient;
    private final JobClient jobClient;
    private final CompanyClient companyClient;

    public void publishStatusChange(Application app,
                                    ApplicationStatus oldStatus,
                                    ApplicationStatus newStatus,
                                    String note) {

        try {
            UserResponse candidate = userClient.getUserById(app.getCandidateId());
            JobResponse job = jobClient.getJobById(app.getJobId());
            CompanyResponse company = companyClient.getCompanyById(app.getCompanyId());

            ApplicationStatusChangedEvent event = ApplicationStatusChangedEvent.builder()
                    .applicationId(app.getId())
                    .candidateId(app.getCandidateId())
                    .candidateEmail(candidate.getEmail())
                    .candidateName(candidate.getFullName())
                    .oldStatus(oldStatus)
                    .newStatus(app.getStatus())
                    .note(note)
                    .jobTitle(job.getTitle())
                    .companyName(company.getName())
                    .changedAt(LocalDateTime.now())
                    .build();

            kafkaTemplate.send(TOPIC, String.valueOf(app.getId()), event);
        } catch (Exception e) {
            System.out.println("Error in publish status change ----- "  +e.getMessage());
        }

    }

}
