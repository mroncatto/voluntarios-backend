package com.voluntarios.resource;

import com.voluntarios.config.HttpResponse;
import com.voluntarios.domain.User;
import com.voluntarios.exception.custom.EmailExistException;
import com.voluntarios.exception.custom.UserNotFoundException;
import com.voluntarios.exception.custom.UsernameExistException;
import com.voluntarios.exception.custom.ValidationException;
import com.voluntarios.security.JwtTokenProvider;
import com.voluntarios.service.UserService;
import freemarker.template.TemplateException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "User", description = "Cadastro y alteración de usuarios")
public class UserRestController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "Obtener todos los usuarios", security = {
            @SecurityRequirement(name = "bearerAuth")}, responses = {
            @ApiResponse(description = "Operación exitosa", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))),
            @ApiResponse(responseCode = "403", description = "Falla de autenticación", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))})
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/user")
    public ResponseEntity<List<User>> indexUser() {
        List<User> usuarios = this.userService.findAll();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @Operation(summary = "Obtener todos los usuarios con paginación", security = {
            @SecurityRequirement(name = "bearerAuth")}, responses = {
            @ApiResponse(description = "Operación exitosa", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))),
            @ApiResponse(responseCode = "403", description = "Falla de autenticación", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))})
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/user/page/{page}")
    public ResponseEntity<Page<User>> indexPageUser(@PathVariable Integer page) {
        Page<User> usuarios = this.userService.findAll(PageRequest.of(page, 4));
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @Operation(summary = "Obtener un usuario", security = {
            @SecurityRequirement(name = "bearerAuth")}, responses = {
            @ApiResponse(description = "Operación exitosa", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "403", description = "Falla de autenticación", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))})
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/user/{username}")
    public ResponseEntity<User> showUser(@PathVariable String username) {
        User user = this.userService.findUserByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Registrarse en la plataforma", responses = {
            @ApiResponse(description = "Operación exitosa", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Requisición incorrecta", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = HttpResponse.class)))),
            @ApiResponse(responseCode = "403", description = "Falla de autenticación", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = HttpResponse.class))))})
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/user")
    public ResponseEntity<?> signIn(@Valid @RequestBody User user, BindingResult result) throws ValidationException, UserNotFoundException, EmailExistException, UsernameExistException, MessagingException, TemplateException, IOException {
        User newuser = this.userService.save(user, result);
        return new ResponseEntity<>(newuser, HttpStatus.OK);
    }

    @Operation(summary = "Alterar perfil", security = {
            @SecurityRequirement(name = "bearerAuth")}, responses = {
            @ApiResponse(description = "Operación exitosa", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Requisición incorrecta", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = HttpResponse.class)))),
            @ApiResponse(responseCode = "403", description = "Falla de autenticación", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = HttpResponse.class))))})
    @ResponseStatus(value = HttpStatus.CREATED)
    @PutMapping("/user/profile")
    public ResponseEntity<?> changeProfile(@RequestHeader(value = "Authorization", required = true) String authorization, @Valid @RequestBody User user, BindingResult result) throws ValidationException, UserNotFoundException, EmailExistException, UsernameExistException {

        // Obtiene el username a partir del token de autenticacion
        String token = authorization.substring("Bearer ".length());
        String username = this.jwtTokenProvider.getSubject(token);

        User updateuser = this.userService.update(username, user, result);
        return new ResponseEntity<>(updateuser, HttpStatus.OK);
    }

    @Operation(summary = "Alterar el password", security = {
            @SecurityRequirement(name = "bearerAuth")}, responses = {
            @ApiResponse(description = "Operación exitosa", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Requisición incorrecta", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = HttpResponse.class)))),
            @ApiResponse(responseCode = "403", description = "Falla de autenticación / Credenciales incorrectas", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = HttpResponse.class))))})
    @ResponseStatus(value = HttpStatus.CREATED)
    @PutMapping("/user/changepassword")
    public ResponseEntity<?> changePassword(@RequestParam String oldpassword,
                                            @RequestParam String newpassword,
                                            @RequestParam String username) throws MessagingException, TemplateException, IOException {

        User updatedUser = this.userService.changePassword(username, newpassword, oldpassword);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

}
