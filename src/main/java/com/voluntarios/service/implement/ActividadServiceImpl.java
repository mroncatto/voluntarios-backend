package com.voluntarios.service.implement;

import com.voluntarios.domain.Actividad;
import com.voluntarios.domain.User;
import com.voluntarios.exception.custom.ValidationException;
import com.voluntarios.repository.ActividadRepository;
import com.voluntarios.repository.UserRepository;
import com.voluntarios.service.ActividadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.voluntarios.config.ActividadConstant.ACTIVIDAD_NOT_FOUND_BY_ID;
import static com.voluntarios.config.UserConstant.USER_NOT_FOUND_BY_USERNAME;

@Service
@RequiredArgsConstructor
public class ActividadServiceImpl implements ActividadService {
    private final ActividadRepository actividadRepository;
    private final UserRepository userRepository;

    @Override
    public List<Actividad> findAll() {
        return this.actividadRepository.findAll();
    }

    @Override
    public Page<Actividad> findAll(Pageable pageable) {
        return this.actividadRepository.findAll(pageable);
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
    public Actividad update(Long id, Actividad actividad, BindingResult result) throws ValidationException {

        if (result.hasErrors())
            throw new ValidationException(Objects.requireNonNull(result.getFieldError().getDefaultMessage()).toUpperCase());

        Actividad currentActividad = this.actividadRepository.findById(id)
                .orElseThrow(() -> new NoResultException(ACTIVIDAD_NOT_FOUND_BY_ID));

        currentActividad.setActividad(actividad.getActividad());
        currentActividad.setDetalle(actividad.getDetalle());
        currentActividad.setInicio(actividad.getInicio());
        currentActividad.setSituacion(actividad.getSituacion());
        currentActividad.setAlterado(new Date());
        return this.actividadRepository.save(currentActividad);
    }

    @Override
    public Actividad suscribe(Long id, String username){
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
        if (!exists)
            voluntarios.add(currentUser);

        currentActividad.setVoluntarios(voluntarios);
        return this.actividadRepository.save(currentActividad);
    }
}
