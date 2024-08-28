package com.srienath.restapp.serviceimpl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendRegistrationEmail(String toEmail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Welcome to LPG Connect");
        message.setText("Dear " + username + ",\n\nThank you for registering with LPG Connect. Your registration is successful!\n\nBest regards,\nLPG Connect Team");

        javaMailSender.send(message);
    }
}

