package com.example.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaConfig {
    // KafkaTemplate уже автоконфигурируется Spring Boot при наличии spring-kafka
    // Добавлять бины не обязательно, но можно инжектить KafkaTemplate<String, Object>
}
