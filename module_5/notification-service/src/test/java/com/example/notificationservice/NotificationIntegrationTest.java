package com.example.notificationservice;

import com.example.notificationservice.email.EmailService;
import com.example.notificationservice.model.NotificationEvent;
import com.example.notificationservice.model.Operation;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = NotificationServiceApplication.class)
@EmbeddedKafka(partitions = 1, topics = {"user.events"}, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class NotificationIntegrationTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(new ServerSetup(3025, null, "smtp"));

    @Autowired
    private KafkaTemplate<String, Object> kafka;

    @Autowired
    private EmailService emailService;

    @Test
    void shouldSendEmailOnCreateEvent() throws Exception {
        String to = "test@example.com";
        kafka.send("user.events", to, new NotificationEvent(Operation.CREATE, to)).get();

        greenMail.waitForIncomingEmail(1);

        var messages = greenMail.getReceivedMessages();
        assertThat(messages).hasSize(1);
        assertThat(messages[0].getAllRecipients()[0].toString()).isEqualTo(to);
        assertThat(messages[0].getSubject()).contains("Аккаунт создан");
    }

    @Test
    void shouldSendEmailOnDeleteEvent() throws Exception {
        String to = "test2@example.com";
        kafka.send("user.events", to, new NotificationEvent(Operation.DELETE, to)).get();

        greenMail.waitForIncomingEmail(1);

        var messages = greenMail.getReceivedMessages();
        assertThat(messages).hasSize(1);
        assertThat(messages[0].getAllRecipients()[0].toString()).isEqualTo(to);
        assertThat(messages[0].getSubject()).contains("Аккаунт удалён");
    }
}
