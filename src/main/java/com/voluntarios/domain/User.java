package com.voluntarios.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.voluntarios.domain.enumerate.UserTipo;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "usuario")
@Data
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    @NotNull(message = "(!) Nombre es obligatorio")
    @Column(length = 75, nullable = false)
    @Size(min = 5, max = 75, message = "(!) El nombre debe contener entre 5 y 75 caracteres")
    private String fullName;

    @NotNull(message = "(!) Correo es obligatorio")
    @Size(min = 5, max = 45, message = "(!) El correo debe contener entre 5 y 45 caracteres")
    @Column(length = 45, nullable = false, unique = true)
    private String email;

    @NotNull(message = "(!) Username es obligatorio")
    @Size(min = 5, max = 25, message = "(!) El username debe contener entre 5 y 25 caracteres")
    @Column(length = 25, nullable = false, unique = true)
    private String username;

    @NotNull(message = "(!) Password es obligatorio")
    @Size(min = 6, message = "(!) El password debe contener minimo 6 caracteres")
    @Column(length = 128, nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "(!) Tipo de usuario es obligatorio")
    @Column(length = 1, nullable = false)
    private UserTipo userTipo;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginDate;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date joinDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private boolean nonLocked;

    @PrePersist
    private void prepresist() {
        this.active = true;
        this.nonLocked = true;
        this.joinDate = new Date();
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "user" })
    private List<Role> roles;
}
