package com.aimbitious.job.controller;

import com.aimbitious.job.event.ApplicationStatusChangedEvent;
import com.aimbitious.job.services.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailNotificationService emailNotificationService;

    @GetMapping("/sent")
    public String notificationController() throws Exception {
//        emailNotificationService.sendStatusChangedEmail();
        return "email sent";
    }
}
