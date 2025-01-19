package com.roshan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String recipientEmail, String subject, String message) {
        System.out.println("Inside mail sending");
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(recipientEmail);       // Recipient email
        mailMessage.setSubject(subject);         // Email subject
        mailMessage.setText(message);            // Email body message
        mailMessage.setFrom("rosanacharya2060@gmail.com"); // Sender email (adjust as needed)

        javaMailSender.send(mailMessage);        // Send the email
    }
}
