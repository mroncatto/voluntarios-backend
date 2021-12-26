package com.voluntarios.service;

import com.voluntarios.domain.Actividad;
import com.voluntarios.exception.custom.ValidationException;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface ActividadService {
    List<Actividad> findAll();
    Actividad findById(Long id);
    Actividad save(Actividad actividad, BindingResult result) throws ValidationException;
    Actividad update(Long id, Actividad actividad, BindingResult result) throws ValidationException;
}
