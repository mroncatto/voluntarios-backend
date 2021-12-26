package com.voluntarios.service.implement;

import com.voluntarios.domain.Actividad;
import com.voluntarios.exception.custom.ValidationException;
import com.voluntarios.repository.ActividadRepository;
import com.voluntarios.service.ActividadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.voluntarios.config.ActividadConstant.ACTIVIDAD_NOT_FOUND_BY_ID;

@Service
@RequiredArgsConstructor
public class ActividadServiceImpl implements ActividadService {
    private final ActividadRepository actividadRepository;

    @Override
    public List<Actividad> findAll() {
        return this.actividadRepository.findAll();
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
        currentActividad.setDetalle(actividad.getActividad());
        currentActividad.setInicio(actividad.getInicio());
        currentActividad.setSituacion(actividad.getSituacion());
        currentActividad.setAlterado(new Date());
        return this.actividadRepository.save(currentActividad);
    }
}
