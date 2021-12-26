package com.voluntarios.resource;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voluntarios.config.AuthResponse;
import com.voluntarios.config.HttpResponse;
import com.voluntarios.config.SecurityConstant;
import com.voluntarios.domain.User;
import com.voluntarios.security.JwtTokenProvider;
import com.voluntarios.security.UserPrincipal;
import com.voluntarios.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Json Web Token")
public class AuthenticationRestController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "Realiza Login", responses = {
            @ApiResponse(description = "Operación exitosa", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "403", description = "Credenciales Incorrectas / Cuenta Bloqueada / Cuenta Inactiva", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = HttpResponse.class)))) })
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/token")
    public String login(@RequestParam String username, @RequestParam String password) {
        /**
         * Esta funcion es manejada por el propio spring mediante CustomAuthenticationFilter
         */
        return "";
    }

    @Operation(summary = "Renueva el token de autenticacion", security = {
            @SecurityRequirement(name = "bearerAuth")}, responses = {
            @ApiResponse(description = "Operación exitosa", responseCode = "200", headers = @Header(name = "Authorization", description = "Json Web Token: Bearer", schema = @Schema(implementation = String.class)), content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisición incorrecta", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = HttpResponse.class)))) })
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                String username = this.jwtTokenProvider.getSubject(refresh_token);
                User user = userService.findUserByUsername(username);
                UserPrincipal userPrincipal = new UserPrincipal(user);
                final String access_token = this.jwtTokenProvider.generateJwtToken(userPrincipal, false);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                tokens.put("token_type", "bearer");
                tokens.put("expires_at", JWT.decode(access_token).getExpiresAt().toString());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                log.error("Error logging in: {}", exception.getMessage());
                response.setHeader("error", exception.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error", SecurityConstant.TOKEN_CANNOT_BE_VERIFIED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("No se encuentra el refresh token.");
        }
    }
}
