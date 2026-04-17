package org.tax.mitra.service.notificationService;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.tax.mitra.model.Notification;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    @Async
    public void sendNotification(Notification request) {
        try {
            if (request == null || request.getEmail() == null) {
                log.warn("Email notification skipped due to null request/email");
                return;
            }
            String email = request.getEmail();
            String message = request.getMessage();
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("Your OTP for Verification - Subal Technologies");
            helper.setText(buildEmailBody(message), true);
            mailSender.send(mimeMessage);
            log.info("OTP email sent successfully to {}", email);
        } catch (Exception e) {
            log.error("Failed to send OTP email to {}", request.getEmail(), e);
        }
    }

    private String buildEmailBody(String otp) {
        return """
            <html>
            <body style="font-family: Arial, sans-serif; background-color:#0b0f17; color:#ffffff; padding:20px;">
                <div style="max-width:500px;margin:auto;background:#111827;padding:20px;border-radius:12px;">
                    <h2 style="color:#f97316;">TaxMitra Verification</h2>
                    <p>Hi,</p>
                    <p>Your One-Time Password (OTP) is:</p>
                    <div style="
                        font-size:24px;
                        letter-spacing:6px;
                        font-weight:bold;
                        background:#1f2937;
                        padding:12px;
                        text-align:center;
                        border-radius:8px;
                        margin:15px 0;
                        color:#ffffff;">
                        %s
                    </div>
                    <p style="color:#cbd5e1;">
                        This OTP is valid for <b>5 minutes</b>.
                        Do not share it with anyone.
                    </p>
                    <hr style="border:0;border-top:1px solid #374151;">
                    <p style="font-size:12px;color:#9ca3af;">
                        If you did not request this, please ignore this email.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(otp);
    }
}