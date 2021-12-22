package com.voluntarios.security;

import com.voluntarios.domain.User;
import com.voluntarios.repository.UserRepository;
import com.voluntarios.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.voluntarios.config.UserConstant.USER_NOT_FOUND_BY_USERNAME;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findUserByUsername(username);
        if(user == null)
            throw new UsernameNotFoundException(USER_NOT_FOUND_BY_USERNAME.concat(" "));

        validateLoginAttempt(user);
        return new UserPrincipal(user);
    }

    private void validateLoginAttempt(User user) {
        if (user.isNonLocked()) {
            user.setNonLocked(!loginAttemptService.hasExceededMaxAttempts(user.getUsername()));
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }
}
