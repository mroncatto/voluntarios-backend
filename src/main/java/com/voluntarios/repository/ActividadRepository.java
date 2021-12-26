package com.voluntarios.repository;

import com.voluntarios.domain.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActividadRepository extends JpaRepository<Actividad, Long> {

}
