package com.hike.service.impl;

import com.hike.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@EnableAutoConfiguration
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment env;

    @Override
    public void sendEmail(String type, String email, String content) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(email);
        helper.setFrom(env.getProperty("mail.from"), "HikeIT");

        switch (type){
            case "reset":
                helper.setSubject(env.getProperty("mail.resetare.subject"));
                break;
            case "newsletter":
                helper.setSubject(env.getProperty("mail.newsletter.subject"));
                break;
            default:
                helper.setSubject("Informare HikeIT");
        }

        helper.setText(content, true);

        javaMailSender.send(message);
    }
}
