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

    @KafkaListener(
            topics = "application.status.changed",
            groupId = "notification-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleStatusChanged(ApplicationStatusChangedEvent event) {
        log.info("Received status-changed event: application={}, newStatus={}",
                event.getApplicationId(), event.getNewStatus());
        emailService.sendStatusChangedEmail(event);
    }

    @KafkaListener(
            topics = "application.note.added",
            groupId = "notification-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleNoteAdded(ApplicationNoteAddedEvent event) {
        log.info("Received note-added event: application={}", event.getApplicationId());
        emailService.sendNoteAddedEmail(event);
    }
}
