package com.example.social_media_plateform.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendVerificationEmail(String to, String token ) {

        String verificationLink= "http://localhost:8080/accounts/verify-email?token="+token;

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        try {
            helper.setTo(to);
            helper.setSubject("Email Verification");
            helper.setText(getVerificationEmailContent(verificationLink), true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace(); // Handle the exception as needed
        }
    }

    private String getVerificationEmailContent(String verificationLink) {
        return "<html><body>" +
                "<h2>Verify Your Email Address</h2>" +
                "<p>Thank you for registering! Please click the link below to verify your email address:</p>" +
                "<p><a href='" + verificationLink + "'>Verify Email</a></p>" +
                "<p>If you didn't register on our site, please ignore this email.</p>" +
                "</body></html>";
    }
}

