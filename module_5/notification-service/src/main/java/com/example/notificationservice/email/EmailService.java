package com.example.notificationservice.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mail;

    public EmailService(JavaMailSender mail) {
        this.mail = mail;
    }

    public void sendCreated(String to) {
        send(to, "Аккаунт создан", "Здравствуйте! Ваш аккаунт на сайте был успешно создан.");
    }

    public void sendDeleted(String to) {
        send(to, "Аккаунт удалён", "Здравствуйте! Ваш аккаунт был удалён.");
    }

    public void send(String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        mail.send(msg);
    }
}