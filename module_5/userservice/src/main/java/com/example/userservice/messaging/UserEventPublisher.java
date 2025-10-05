package com.example.userservice.messaging;

import com.example.userservice.events.NotificationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher {

    public static final String TOPIC = "user.events";
    private final KafkaTemplate<String, Object> kafka;

    public UserEventPublisher(KafkaTemplate<String, Object> kafka) {
        this.kafka = kafka;
    }

    public void publish(NotificationEvent event) {
        kafka.send(TOPIC, event.getEmail(), event);
    }
}