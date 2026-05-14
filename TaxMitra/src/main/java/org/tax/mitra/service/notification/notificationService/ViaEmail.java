package org.tax.mitra.service.notification.notificationService;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.tax.mitra.model.Notification;

import java.util.Objects;

@Slf4j
@Service("emailService")
@RequiredArgsConstructor
public class ViaEmail extends NotificationService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    @Override
    public void sendNotification(Notification request) {

        validate(request);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(request.getEmail());
            helper.setSubject(resolveSubject(request));
            helper.setText(buildEmailBody(request), true);

            mailSender.send(mimeMessage);

            log.info("Email sent successfully to {}", request.getEmail());

        } catch (Exception ex) {
            log.error("Failed to send email to {}", request.getEmail(), ex);
            throw new RuntimeException("Email sending failed", ex);
        }
    }

    private void validate(Notification request) {

        if (request == null) {
            throw new IllegalArgumentException("Notification request cannot be null");
        }

        if (Objects.isNull(request.getEmail()) || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required for EMAIL channel");
        }

        if (Objects.isNull(request.getMessage()) || request.getMessage().isBlank()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }

        if (Objects.isNull(request.getNotificationType())) {
            throw new IllegalArgumentException("Notification type is required");
        }
    }


    private String resolveSubject(Notification request) {

        return switch (request.getNotificationType()) {
            case OTP -> "Your OTP for Verification - TaxMitra";
            case TRANSACTIONAL -> "Transaction Update - TaxMitra";
            case ALERT -> "Important Alert - TaxMitra";
            case PROMOTIONAL -> "Latest Offers from TaxMitra";
        };
    }

    private String buildEmailBody(Notification request) {

        return switch (request.getNotificationType()) {

            case OTP -> buildOtpEmail(request.getMessage());

            case TRANSACTIONAL, ALERT, PROMOTIONAL ->
                    buildGenericEmail(request.getMessage());
        };
    }

    private String buildOtpEmail(String otp) {
        return """
            <html>
            <body style="font-family: Arial, sans-serif; background-color:#0b0f17; color:#ffffff; padding:20px;">
                <div style="max-width:500px;margin:auto;background:#111827;padding:20px;border-radius:12px;">
                    <h2 style="color:#f97316;">TaxMitra Verification</h2>
                    <p>Your One-Time Password (OTP) is:</p>
                    <div style="font-size:24px;letter-spacing:6px;font-weight:bold;background:#1f2937;
                                padding:12px;text-align:center;border-radius:8px;margin:15px 0;">
                        %s
                    </div>
                    <p>This OTP is valid for <b>5 minutes</b>. Do not share it.</p>
                </div>
            </body>
            </html>
            """.formatted(otp);
    }

    private String buildGenericEmail(String message) {
        return """
            <html>
            <body style="font-family: Arial, sans-serif;">
                <p>%s</p>
            </body>
            </html>
            """.formatted(message);
    }
}