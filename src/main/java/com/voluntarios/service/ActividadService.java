package com.voluntarios.service;

import com.voluntarios.domain.Actividad;
import com.voluntarios.domain.User;
import com.voluntarios.exception.custom.ValidationException;
import freemarker.template.TemplateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface ActividadService {
    List<Actividad> findAll();
    Page<Actividad> findAll(Pageable pageable);
    List<Actividad> findByVoluntario(Long id);
    List<Actividad> findByCreadoPor(Long id);
    Actividad findById(Long id);
    Actividad save(Actividad actividad, BindingResult result) throws ValidationException;
    Actividad update(Long id, Actividad actividad, BindingResult result) throws ValidationException, MessagingException, TemplateException, IOException;
    Actividad suscribe(Long id, String username) throws MessagingException, TemplateException, IOException;

}
