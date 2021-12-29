package com.voluntarios.service.implement;

import com.voluntarios.domain.Actividad;
import com.voluntarios.domain.User;
import com.voluntarios.service.EmailService;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final Configuration configuration;
    private final JavaMailSender javaMailSender;


    @Override
    public void welcome(User user) throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Bienvenido a la a plataforma");
        helper.setFrom("noreplay@voluntarios.com");
        helper.setTo(user.getEmail());
        String emailContent = this.welcomeTemplate(user);
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
    }

    @Override
    public void nuevoVoluntario(User user, User voluntario, String actividad) throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject(actividad.concat(" - Nuevo voluntario"));
        helper.setFrom("noreplay@voluntarios.com");
        helper.setTo(user.getEmail());
        String emailContent = this.nuevoVoluntarioTemplate(user, voluntario, actividad);
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
    }

    @Override
    public void actividadAlterada(String[] users, String actividad_name, Actividad actividad) throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject(actividad_name.concat(" - Actividad alterada"));
        helper.setFrom("noreplay@voluntarios.com");
        helper.setTo(users);
        String emailContent = this.actividadAlteradaTempalte(actividad_name, actividad);
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
    }

    private String welcomeTemplate(User user) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getFullName());
        model.put("fecha", new Date().toString());
        configuration.getTemplate("welcome.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }

    private String nuevoVoluntarioTemplate(User user, User voluntario, String actividad) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getFullName());
        model.put("actividad", actividad);
        model.put("voluntario", voluntario.getFullName());
        model.put("fecha", new Date().toString());
        configuration.getTemplate("nuevo_voluntario.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }

    private String actividadAlteradaTempalte(String actividad_name, Actividad actividad) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("actividad", actividad_name);
        model.put("nombre_actividad", actividad.getActividad());
        model.put("inicio_actividad", actividad.getInicio().toString());
        model.put("situacion_actividad", actividad.getSituacion());
        model.put("fecha", new Date().toString());
        configuration.getTemplate("actividad_alterada.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }
}
