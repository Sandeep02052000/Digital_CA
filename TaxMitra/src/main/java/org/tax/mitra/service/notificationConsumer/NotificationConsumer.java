//package org.tax.mitra.service.notificationConsumer;
//
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//public class NotificationConsumer {
//
//    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
//    public void consume(String message) {
//        System.out.println("Received notification: " + message);
//
//        // TODO:
//        // - Send SMS
//        // - Send Email
//        // - Push notification
//    }
//}