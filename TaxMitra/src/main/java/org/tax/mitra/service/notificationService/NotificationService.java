//package org.tax.mitra.service.notificationService;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//@Service
//public class NotificationService {
//    private final KafkaTemplate<String, String> kafkaTemplate;
//    private static final String TOPIC = "notification";
//
//    public NotificationService(KafkaTemplate<String, String> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    public void sendNotification(Map<String, Object> response) {
//        String message = convertToJson(response);
//        kafkaTemplate.send(TOPIC, message);
//    }
//    private String convertToJson(Map<String, Object> response) {
//        try {
//            return new ObjectMapper().writeValueAsString(response);
//        } catch (Exception e) {
//            throw new RuntimeException("Error converting message", e);
//        }
//    }
//}