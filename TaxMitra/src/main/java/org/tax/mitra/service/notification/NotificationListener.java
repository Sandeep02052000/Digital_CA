package org.tax.mitra.service.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.tax.mitra.model.Notification;
import org.tax.mitra.service.notification.notificationService.NotificationService;

@Component
public class NotificationListener {
    private static final Logger logger = LoggerFactory.getLogger(NotificationListener.class);

    private final NotificationService emailService;
    private final NotificationService smsService;
    private final NotificationService urlService;
    @Autowired
    public NotificationListener(@Qualifier("emailService") NotificationService emailService,
                                @Qualifier("smsService") NotificationService smsService,
                                @Qualifier("urlService") NotificationService urlService) {
        this.emailService = emailService;
        this.smsService = smsService;
        this.urlService = urlService;
    }

    public void prepareNotificationToSend(Notification notification) {
        if(!notification.isValid()) {
            logger.error("Invalid notification request :: {}",notification);
            return;
        }
        switch (notification.getType()) {
            case EMAIL -> emailService.sendNotification(notification);
            case SMS -> smsService.sendNotification(notification);
            case URL -> urlService.sendNotification(notification);
        }
    }
}
