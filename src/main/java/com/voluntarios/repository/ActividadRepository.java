package com.voluntarios.repository;

import com.voluntarios.domain.Actividad;
import com.voluntarios.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActividadRepository extends JpaRepository<Actividad, Long> {
    List<Actividad> findByVoluntarios(User user);
    List<Actividad> findByCreadoPor(User user);
}
