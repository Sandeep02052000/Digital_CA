package org.tax.mitra.service.notification.notificationService;

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
public abstract class NotificationService {

    public abstract void sendNotification(Notification request);

}