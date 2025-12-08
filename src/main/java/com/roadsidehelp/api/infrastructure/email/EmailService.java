package com.roadsidehelp.api.infrastructure.email;

public interface EmailService {
    void send(String to, String subject, String message);
}
