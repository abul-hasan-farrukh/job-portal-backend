package com.aimbitious.job.services;

import com.aimbitious.job.domain.ApplicationStatus;
import com.aimbitious.job.event.ApplicationNoteAddedEvent;
import com.aimbitious.job.event.ApplicationStatusChangedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class EmailNotificationService {

    private static final String EMAILJS_API =
            "https://api.emailjs.com/api/v1.0/email/send";

    private static final Map<ApplicationStatus, String> STATUS_LABELS =
            Map.of(
                    ApplicationStatus.PENDING, "Pending Review",
                    ApplicationStatus.REVIEWING, "Under Review",
                    ApplicationStatus.SHORTLISTED, "Shortlisted",
                    ApplicationStatus.INTERVIEW_SCHEDULED, "Interview Scheduled",
                    ApplicationStatus.REJECTED, "Not Selected",
                    ApplicationStatus.HIRED, "Hired",
                    ApplicationStatus.WITHDRAWN, "Withdrawn"
            );

    private static final Map<ApplicationStatus, String> STATUS_COLORS =
            Map.of(
                    ApplicationStatus.PENDING, "#f59e0b",
                    ApplicationStatus.REVIEWING, "#3b82f6",
                    ApplicationStatus.SHORTLISTED, "#8b5cf6",
                    ApplicationStatus.INTERVIEW_SCHEDULED, "#06b6d4",
                    ApplicationStatus.REJECTED, "#ef4444",
                    ApplicationStatus.HIRED, "#22c55e",
                    ApplicationStatus.WITHDRAWN, "#6b7280"
            );

    private final RestClient restClient = RestClient.create();

    @Value("${emailjs.service-id}")
    private String serviceId;

    @Value("${emailjs.template-id}")
    private String templateId;

    @Value("${emailjs.public-key}")
    private String publicKey;

    @Value("${emailjs.private-key}")
    private String privateKey;


    // --------------------------------------------------
    // 1. APPLICATION STATUS CHANGED EMAIL
    // --------------------------------------------------

    public void sendStatusChangedEmail(
            ApplicationStatusChangedEvent event) {

        ApplicationStatus newStatus = event.getNewStatus();

        String statusLabel = newStatus == null
                ? "Updated"
                : STATUS_LABELS.getOrDefault(
                newStatus, newStatus.name()
        );

        String statusColor = newStatus == null
                ? "#6b7280"
                : STATUS_COLORS.getOrDefault(
                newStatus, "#6b7280"
        );

        String subject = "Application update: "
                + safe(event.getJobTitle())
                + " at "
                + safe(event.getCompanyName());

        String htmlBody = buildStatusChangeHtml(
                event,
                statusColor,
                statusLabel
        );

        sendEmail(
                event.getCandidateEmail(),
                event.getCandidateName(),
                subject,
                htmlBody
        );
    }


    // --------------------------------------------------
    // 2. APPLICATION NOTE ADDED EMAIL
    // --------------------------------------------------

    public void sendNoteAddedEmail(
            ApplicationNoteAddedEvent event) {

        String subject = "New update on your application: "
                + safe(event.getJobTitle());

        String htmlBody = buildNoteAddedHtml(event);

        sendEmail(
                event.getCandidateEmail(),
                event.getCandidateName(),
                subject,
                htmlBody
        );
    }


    // --------------------------------------------------
    // 3. COMMON EMAILJS API METHOD
    // --------------------------------------------------

    private void sendEmail(
            String recipient,
            String recipientName,
            String subject,
            String htmlBody) {

        if (recipient == null || recipient.isBlank()) {
            throw new IllegalArgumentException(
                    "Recipient email address cannot be empty"
            );
        }

        Map<String, Object> templateParams = new HashMap<>();

        templateParams.put("to_email", recipient);
        templateParams.put("to_name", safe(recipientName));
        templateParams.put("subject", subject);
        templateParams.put("message_html", htmlBody);

        Map<String, Object> request = new HashMap<>();

        request.put("service_id", serviceId);
        request.put("template_id", templateId);
        request.put("user_id", publicKey);
        request.put("accessToken", privateKey);
        request.put("template_params", templateParams);

        try {

            restClient.post()
                    .uri(EMAILJS_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();

            log.info(
                    "EmailJS successfully accepted email request. Recipient: {}, Subject: {}",
                    recipient,
                    subject
            );

        } catch (Exception ex) {

            log.error(
                    "Failed to send email through EmailJS. Recipient: {}, Error: {}",
                    recipient,
                    ex.getMessage()
            );

            // Propagate the exception so Kafka does not
            // silently acknowledge a failed email operation.
            throw ex;
        }
    }


    // --------------------------------------------------
    // 4. APPLICATION STATUS EMAIL HTML
    // --------------------------------------------------

    private String buildStatusChangeHtml(
            ApplicationStatusChangedEvent event,
            String statusColor,
            String statusLabel) {

        return "<!DOCTYPE html>"
                + "<html><body style='font-family:Arial,sans-serif;"
                + "background:#f3f4f6;margin:0;padding:20px;'>"

                + "<div style='max-width:600px;margin:0 auto;"
                + "background:#fff;border-radius:8px;overflow:hidden;'>"

                + "<div style='background:" + statusColor
                + ";padding:24px;text-align:center;'>"

                + "<h1 style='color:#fff;margin:0;font-size:22px;'>"
                + "Application Status Update</h1></div>"

                + "<div style='padding:32px;'>"

                + "<p style='color:#374151;font-size:16px;'>Hi <strong>"
                + escapeHtml(event.getCandidateName())
                + "</strong>,</p>"

                + "<p style='color:#6b7280;'>Your application for <strong>"
                + escapeHtml(event.getJobTitle())
                + "</strong> at <strong>"
                + escapeHtml(event.getCompanyName())
                + "</strong> has been updated.</p>"

                + "<div style='background:#f9fafb;"
                + "border:1px solid #e5e7eb;border-radius:8px;"
                + "padding:20px;margin:20px 0;text-align:center;'>"

                + "<p style='margin:0 0 8px;color:#9ca3af;"
                + "font-size:13px;text-transform:uppercase;'>"
                + "New Status</p>"

                + "<span style='display:inline-block;"
                + "background:" + statusColor
                + ";color:#fff;padding:8px 20px;"
                + "border-radius:20px;font-weight:bold;'>"
                + escapeHtml(statusLabel)
                + "</span></div>"

                + "<p style='color:#6b7280;'>"
                + "You can log in to Aimbitious to view "
                + "your full application timeline.</p>"

                + "</div>"

                + buildFooter()
                + "</div></body></html>";
    }


    // --------------------------------------------------
    // 5. APPLICATION NOTE EMAIL HTML
    // --------------------------------------------------

    private String buildNoteAddedHtml(
            ApplicationNoteAddedEvent event) {

        String addedAt = event.getAddedAt() == null
                ? "Recently"
                : event.getAddedAt().format(
                DateTimeFormatter.ofPattern(
                        "dd MMM yyyy, hh:mm a"
                )
        );

        return "<!DOCTYPE html>"
                + "<html><body style='font-family:Arial,sans-serif;"
                + "background:#f3f4f6;margin:0;padding:20px;'>"

                + "<div style='max-width:600px;margin:0 auto;"
                + "background:#fff;border-radius:8px;overflow:hidden;'>"

                + "<div style='background:#3b82f6;"
                + "padding:24px;text-align:center;'>"

                + "<h1 style='color:#fff;margin:0;font-size:22px;'>"
                + "New Application Update</h1></div>"

                + "<div style='padding:32px;'>"

                + "<p style='color:#374151;font-size:16px;'>Hi <strong>"
                + escapeHtml(event.getCandidateName())
                + "</strong>,</p>"

                + "<p style='color:#6b7280;'>"
                + "There is a new update regarding your job application.</p>"

                + "<div style='background:#f9fafb;"
                + "border:1px solid #e5e7eb;border-radius:8px;"
                + "padding:20px;margin:20px 0;'>"

                + "<p style='margin:0 0 12px;color:#374151;'>"
                + "<strong>Job Position:</strong> "
                + escapeHtml(event.getJobTitle())
                + "</p>"

                + "<p style='margin:0 0 12px;color:#374151;'>"
                + "<strong>Company:</strong> "
                + escapeHtml(event.getCompanyName())
                + "</p>"

                + "<p style='margin:0;color:#374151;'>"
                + "<strong>Update Date:</strong> "
                + escapeHtml(addedAt)
                + "</p>"

                + "</div>"

                + "<p style='color:#6b7280;'>"
                + "Please log in to your Aimbitious account "
                + "to review your application updates.</p>"

                + "</div>"

                + buildFooter()
                + "</div></body></html>";
    }


    // --------------------------------------------------
    // 6. COMMON EMAIL FOOTER
    // --------------------------------------------------

    private String buildFooter() {

        return "<div style='background:#f9fafb;padding:16px;"
                + "text-align:center;border-top:1px solid #e5e7eb;'>"

                + "<p style='margin:0;color:#9ca3af;font-size:12px;'>"
                + "Aimbitious &mdash; Your career journey starts here."
                + "</p></div>";
    }


    // --------------------------------------------------
    // 7. HTML ESCAPING AND NULL HANDLING
    // --------------------------------------------------

    private String escapeHtml(String text) {

        if (text == null) {
            return "";
        }

        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}