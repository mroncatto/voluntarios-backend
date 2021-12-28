package com.voluntarios.filter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voluntarios.config.HttpResponse;
import com.voluntarios.domain.User;
import com.voluntarios.repository.UserRepository;
import com.voluntarios.security.JwtTokenProvider;
import com.voluntarios.security.UserPrincipal;
import com.voluntarios.service.LoginAttemptService;
import com.voluntarios.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final LoginAttemptService loginAttemptService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserPrincipal user = (UserPrincipal) authResult.getPrincipal();

        // Obtiene el usuario para injectar en el token y actualiza sus datos
        User account = this.userRepository.findUserByUsername(user.getUsername());
        account.setLastLoginDate(new Date());
        this.userRepository.save(account);

        this.loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        final String access_token = this.jwtTokenProvider.generateJwtToken(user, false);
        final String refresh_token = this.jwtTokenProvider.generateJwtToken(user, true);
        Map<String, Object> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        tokens.put("token_type", "bearer");
        tokens.put("expires_at", JWT.decode(access_token).getExpiresAt().toString());
        tokens.put("user", account);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String username = request.getParameter("username");
        if (username != null)
            this.loginAttemptService.addUserToLoginAttemptCache(username);

        HttpResponse res = new HttpResponse(401, HttpStatus.UNAUTHORIZED, failed.getMessage().toUpperCase() ,failed.getMessage().toUpperCase());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(401);
        new ObjectMapper().writeValue(response.getOutputStream(), res);
    }
}
