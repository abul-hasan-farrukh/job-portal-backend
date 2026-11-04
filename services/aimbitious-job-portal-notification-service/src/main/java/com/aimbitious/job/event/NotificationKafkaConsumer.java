package com.aimbitious.job.event;

import com.aimbitious.job.service.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationKafkaConsumer {

    private final EmailNotificationService emailService;

    // Application status changed notification
    @KafkaListener(
            topics = "application.status.changed",
            groupId = "notification-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleStatusChanged(
            ApplicationStatusChangedEvent event) {

        log.info(
                "Received application status event. Application={}, Candidate={}, Status={}, Email={}",
                event.getApplicationId(),
                event.getCandidateId(),
                event.getNewStatus(),
                event.getCandidateEmail()
        );

        try {

            emailService.sendStatusChangedEmail(event);

            log.info(
                    "Status-change email processing completed for application {}",
                    event.getApplicationId()
            );

        } catch (Exception e) {

            log.error(
                    "Failed to process status-change email for application {}",
                    event.getApplicationId(),
                    e
            );

            throw e;
        }
    }


    // Application note added notification
    @KafkaListener(
            topics = "application.note.added",
            groupId = "notification-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleNoteAdded(
            ApplicationNoteAddedEvent event) {

        log.info(
                "Received note-added event: application={}",
                event.getApplicationId()
        );

        emailService.sendNoteAddedEmail(event);
    }
}