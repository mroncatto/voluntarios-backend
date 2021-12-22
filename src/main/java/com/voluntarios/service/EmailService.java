package com.voluntarios.service;

import com.voluntarios.domain.User;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface EmailService {

    void sendEmail(User user, String subject, String message) throws MessagingException,
            IOException, TemplateException;

    void sendEmails(List<User> users, String subject, String message) throws MessagingException,
            IOException, TemplateException;
}
