package com.voluntarios.service.implement;

import com.voluntarios.domain.Actividad;
import com.voluntarios.domain.User;
import com.voluntarios.exception.custom.ValidationException;
import com.voluntarios.repository.ActividadRepository;
import com.voluntarios.repository.UserRepository;
import com.voluntarios.service.ActividadService;
import com.voluntarios.service.EmailService;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.mail.MessagingException;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.voluntarios.config.ActividadConstant.ACTIVIDAD_NOT_FOUND_BY_ID;
import static com.voluntarios.config.UserConstant.USER_NOT_FOUND;
import static com.voluntarios.config.UserConstant.USER_NOT_FOUND_BY_USERNAME;

@Service
@RequiredArgsConstructor
public class ActividadServiceImpl implements ActividadService {
    private final ActividadRepository actividadRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Override
    public List<Actividad> findAll() {
        return this.actividadRepository.findAll();
    }

    @Override
    public Page<Actividad> findAll(Pageable pageable) {
        return this.actividadRepository.findAll(pageable);
    }

    @Override
    public List<Actividad> findByVoluntario(Long id) {

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new NoResultException(USER_NOT_FOUND));

        return this.actividadRepository.findByVoluntarios(user);
    }

    @Override
    public List<Actividad> findByCreadoPor(Long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new NoResultException(USER_NOT_FOUND));

        return this.actividadRepository.findByCreadoPor(user);
    }

    @Override
    public Actividad findById(Long id) {
        return this.actividadRepository.findById(id)
                .orElseThrow(() -> new NoResultException(ACTIVIDAD_NOT_FOUND_BY_ID));
    }

    @Override
    public Actividad save(Actividad actividad, BindingResult result) throws ValidationException {

        if (result.hasErrors())
            throw new ValidationException(Objects.requireNonNull(result.getFieldError().getDefaultMessage()).toUpperCase());

        return this.actividadRepository.save(actividad);
    }

    @Override
    public Actividad update(Long id, Actividad actividad, BindingResult result) throws ValidationException, MessagingException, TemplateException, IOException {

        if (result.hasErrors())
            throw new ValidationException(Objects.requireNonNull(result.getFieldError().getDefaultMessage()).toUpperCase());

        // Valida si existe la actividad
        Actividad currentActividad = this.actividadRepository.findById(id)
                .orElseThrow(() -> new NoResultException(ACTIVIDAD_NOT_FOUND_BY_ID));

        // Obtiene el nombre de la actividad antes de alterar para utilizar en el correo
        String currentActividadName = currentActividad.getActividad();

        currentActividad.setActividad(actividad.getActividad());
        currentActividad.setDetalle(actividad.getDetalle());
        currentActividad.setInicio(actividad.getInicio());
        currentActividad.setSituacion(actividad.getSituacion());
        currentActividad.setAlterado(new Date());

        /**
         * Recurre el listado de voluntarios de la actividad para enviar
         * un correo a cada un de ellos !
         */
        List<User> users = currentActividad.getVoluntarios();
        String[] voluntarios = new String[currentActividad.getVoluntarios().size()];
        users.forEach(user -> {
            voluntarios[users.indexOf(user)] = user.getEmail();
        });
        // Envia el correo
        this.emailService.actividadAlterada(voluntarios, currentActividadName, currentActividad);

        return this.actividadRepository.save(currentActividad);
    }

    @Override
    public Actividad suscribe(Long id, String username) throws MessagingException, TemplateException, IOException {
        // Valida si existe la actividad
        Actividad currentActividad = this.actividadRepository.findById(id)
                .orElseThrow(() -> new NoResultException(ACTIVIDAD_NOT_FOUND_BY_ID));

        // Valida si existe el usuario
        User currentUser = this.userRepository.findUserByUsername(username);
        if (currentUser == null)
            throw new UsernameNotFoundException(USER_NOT_FOUND_BY_USERNAME + username);

        // Obtiene los voluntarios existente
        List<User> voluntarios = currentActividad.getVoluntarios();

        // Recurre los voluntarios y si existe remueve
        boolean exists = false;
        for (User user : voluntarios) {
            if (user == currentUser) {
                voluntarios.remove(voluntarios.indexOf(currentUser));
                exists = true;
                break;
            }
        }

        // si no existe lo agrega
        if (!exists) {
            voluntarios.add(currentUser);
            // Notifica por correo el creado de la actividad del nuevo voluntario
            this.emailService.nuevoVoluntario(currentActividad.getCreadoPor(), currentUser, currentActividad.getActividad());
        }

        //Agrega el listado de voluntarios actualizado para la persistencia y salva la tarea
        currentActividad.setVoluntarios(voluntarios);
        return this.actividadRepository.save(currentActividad);
    }
}
