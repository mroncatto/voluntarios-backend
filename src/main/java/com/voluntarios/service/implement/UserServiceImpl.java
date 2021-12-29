package com.voluntarios.service.implement;

import com.voluntarios.domain.Role;
import com.voluntarios.domain.User;
import com.voluntarios.exception.custom.*;
import com.voluntarios.repository.RoleRepository;
import com.voluntarios.repository.UserRepository;
import com.voluntarios.service.EmailService;
import com.voluntarios.service.LoginAttemptService;
import com.voluntarios.service.UserService;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.mail.MessagingException;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.voluntarios.config.ExceptionConstant.ROLE_NOT_FOUND_BY;
import static com.voluntarios.config.UserConstant.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;


    @Override
    public List<User> findAll() {
        return this.userRepository.findAll().stream()
                .filter((User user) -> !user.getUsername().equalsIgnoreCase("admin")).collect(Collectors.toList());
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return this.userRepository.findByUsernameNot("admin",pageable);
    }

    @Override
    public User save(User user, BindingResult result) throws EmailExistException, UsernameExistException, ValidationException, UserNotFoundException, MessagingException, TemplateException, IOException {

        if (result.hasErrors())
            throw new ValidationException(Objects.requireNonNull(result.getFieldError()
                    .getDefaultMessage()).toUpperCase());

        validateUsernameAndEmail(StringUtils.EMPTY, user.getUsername(), user.getEmail());

        User newuser = new User();
        user.setPassword(this.encodePassword(user.getPassword()));
        newuser = userRepository.save(user);

        // Envia un correo de bienvenida y salva el usuario
        this.emailService.welcome(user);
        return this.userRepository.save(newuser);
    }

    @Override
    public User update(String username, User user, BindingResult result) throws EmailExistException, UsernameExistException, ValidationException, UserNotFoundException {

        if (result.hasErrors())
            throw new ValidationException(Objects.requireNonNull(result.getFieldError()
                    .getDefaultMessage()).toUpperCase());

        // Realiza validaciones del registro del usuario
        User currentUser = validateUsernameAndEmail(username, user.getUsername(), user.getEmail());

        if (currentUser != null) {
            currentUser.setFullName(user.getFullName());
            currentUser.setEmail(user.getEmail());
            currentUser.setCiudad(user.getCiudad());
            currentUser = userRepository.save(currentUser);
            return this.userRepository.save(currentUser);
        } else {
            return null;
        }
    }

    @Override
    public User findUserByUsername(String username) {
        return this.userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return this.userRepository.findUserByEmail(email);
    }

    @Override
    public User changePassword(String username, String password, String oldpassword) throws UsernameNotFoundException, MessagingException, TemplateException, IOException {
        User user = this.userRepository.findUserByUsername(username);

        if (user == null)
            throw new UsernameNotFoundException(USER_NOT_FOUND_BY_USERNAME + username);

        // Valida las credenciales informadas
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldpassword));

        // Altera el password
        user.setPassword(this.encodePassword(password));
        User updatedUser = this.userRepository.save(user);
        return updatedUser;
    }

    @Override
    public User resetPassword(String email) throws EmailNotFoundException, MessagingException, TemplateException, IOException {
        return null;
    }

    @Override
    public User updateRole(String username, Role role) throws UsernameNotFoundException {
        User currentUser = this.userRepository.findUserByUsername(username);
        Role currentRole = this.roleRepository.findById(role.getId())
                .orElseThrow(() -> new NoResultException(ROLE_NOT_FOUND_BY));

        if (currentUser == null)
            throw new UsernameNotFoundException(USER_NOT_FOUND_BY_USERNAME);

        List<Role> roles = currentUser.getRoles();

        // Recurre los roles y si existe remueve
        boolean exists = false;
        for (Role rol : roles) {
            if (rol == currentRole) {
                roles.remove(roles.indexOf(currentRole));
                exists = true;
                break;
            }
        }

        // si no existe lo agrega
        if (!exists)
            roles.add(currentRole);

        currentUser.setRoles(roles);
        return this.userRepository.save(currentUser);
    }


    // Encriptacion de password
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }


    // Valida que el usuario no esta bloqueado o limpia el cache
    private void validateLoginAttempt(User user) {
        if (user.isNonLocked()) {
            user.setNonLocked(!loginAttemptService.hasExceededMaxAttempts(user.getUsername()));
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }


    // Validacion para el registro de usuarios
    private User validateUsernameAndEmail(String currentUsername, String username, String email)
            throws EmailExistException, UsernameExistException, UserNotFoundException {
        User userByNewUsername = this.findUserByUsername(username);
        User userByNewEmail = this.findUserByEmail(email);

        // Nuevo usuario o update
        if (StringUtils.isNotBlank(currentUsername)) {
            User currentUser = userRepository.findUserByUsername(currentUsername);

            // No se encuentra el usuario mediante el username indicado
            if (currentUser == null)
                throw new UserNotFoundException(USER_NOT_FOUND_BY_USERNAME + currentUsername);

            // El username indicado ya esta en uso
            if (userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId()))
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);

            // El correo indicado ya esta en uso
            if (userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId()))
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);

            return currentUser;

        } else {

            // El correo indicado ya esta en uso
            if (userByNewEmail != null)
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);

            // El username indicado ya esta en uso
            if (userByNewUsername != null)
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);

            return null;
        }

    }
}
