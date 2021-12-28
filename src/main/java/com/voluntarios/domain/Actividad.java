package com.voluntarios.domain;

import com.voluntarios.domain.enumerate.Situacion;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table

public class Actividad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "(!) Nombre de la actividad es obligatorio")
    @Column(length = 75, nullable = false)
    @Size(min = 5, max = 75, message = "(!) El nombre de la actividad debe contener entre 5 y 75 caracteres")
    private String actividad;

    @Column(columnDefinition="text", nullable = false)
    @Size(min = 10, message = "(!) El detalle de la actividad debe contener como maximo 300 caracteres")
    private String detalle;

    @Column(length = 1, nullable = false)
    @NotNull(message = "(!) Situacion es obligatorio")
    private Situacion situacion;

    @ManyToOne(optional = false)
    @NotNull(message = "(!) Usuario autor es obligatorio")
    private User creadoPor;

    @Column(nullable = false)
    @NotNull(message = "(!) Fecha de inicio es obligatorio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inicio;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creado;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date alterado;

    @ManyToMany()
    private List<User> voluntarios;

    @PrePersist
    private void prepresist() {
        this.alterado = new Date();
        this.creado = new Date();
    }
}
