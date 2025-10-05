package com.example.notificationservice.kafka;

import com.example.notificationservice.email.EmailService;
import com.example.notificationservice.model.NotificationEvent;
import com.example.notificationservice.model.Operation;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {

    private final EmailService email;

    public UserEventListener(EmailService email) {
        this.email = email;
    }

    @KafkaListener(topics = "user.events", groupId = "notification-service")
    public void onEvent(NotificationEvent event) {
        if (event.getOperation() == Operation.CREATE) {
            email.sendCreated(event.getEmail());
        } else if (event.getOperation() == Operation.DELETE) {
            email.sendDeleted(event.getEmail());
        }
    }
}