package com.voluntarios.service;

import com.voluntarios.domain.Actividad;
import com.voluntarios.domain.User;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface EmailService {

    void welcome(User user) throws MessagingException,
            IOException, TemplateException;

    void nuevoVoluntario(User user, User voluntario, String actividad) throws MessagingException,
            IOException, TemplateException;

    void actividadAlterada(String[] users, String actividad_name, Actividad actividad) throws MessagingException,
            IOException, TemplateException;
}
