package com.example.notificationservice.model;

public class NotificationEvent {
    private Operation operation;
    private String email;
    public NotificationEvent() {}
    public NotificationEvent(Operation operation, String email) {
        this.operation = operation; this.email = email;
    }
    public Operation getOperation() { return operation; }
    public void setOperation(Operation operation) { this.operation = operation; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
