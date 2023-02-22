package com.hike.service;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface MailService {
    void sendEmail (String type, String email, String content) throws MessagingException, UnsupportedEncodingException;
}
