package com.example.notificationservice.api;

import com.example.notificationservice.email.EmailService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notify")
public class NotificationController {

    private final EmailService email;

    public NotificationController(EmailService email) {
        this.email = email;
    }

    public static class NotifyRequest {
        @NotBlank @Email
        public String email;
        @NotBlank
        public String operation; // CREATE or DELETE
    }

    @PostMapping
    public ResponseEntity<Void> notify(@RequestBody NotifyRequest r) {
        switch (r.operation) {
            case "CREATE" -> email.sendCreated(r.email);
            case "DELETE" -> email.sendDeleted(r.email);
            default -> { return ResponseEntity.badRequest().build(); }
        }
        return ResponseEntity.accepted().build();
    }
}
