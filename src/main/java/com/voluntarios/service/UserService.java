package com.voluntarios.service;

import com.voluntarios.domain.Role;
import com.voluntarios.domain.User;
import com.voluntarios.exception.custom.*;
import freemarker.template.TemplateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface UserService {

    List<User> findAll();

    Page<User> findAll(Pageable pageable);

    User save(User user, BindingResult result) throws EmailExistException, UsernameExistException,
            ValidationException, UserNotFoundException, MessagingException, TemplateException, IOException;

    User update(String username, User user, BindingResult result) throws EmailExistException, UsernameExistException,
            ValidationException, UserNotFoundException;

    User findUserByUsername(String username);
    User findUserByEmail(String email);
    User changePassword(String username, String password, String oldpassword) throws UsernameNotFoundException, MessagingException, TemplateException, IOException;
    User resetPassword(String email) throws EmailNotFoundException, MessagingException, TemplateException, IOException;
    User updateRole(String username, Role role) throws UsernameNotFoundException;

}
