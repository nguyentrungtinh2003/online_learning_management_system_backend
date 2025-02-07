package com.TrungTinhBackend.codearena_backend.Service.Email;

import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;

@Configuration
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public APIResponse sendEmail(String to, String subject, String body) {
        APIResponse apiResponse = new APIResponse();

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Sending mail success !");
            apiResponse.setData(message);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;
    }
}
