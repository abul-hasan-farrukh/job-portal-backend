package com.aimbitious.job.event;

import com.aimbitious.job.services.EmailNotificationService;
import lombok.RequiredArgsConstructor;
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
    public void handleStatusChanged(ApplicationStatusChangedEvent event) throws Exception {
        System.out.println("received------------ "+event);
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
