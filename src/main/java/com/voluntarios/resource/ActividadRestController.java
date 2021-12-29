package com.voluntarios.resource;

import com.voluntarios.config.HttpResponse;
import com.voluntarios.domain.Actividad;
import com.voluntarios.exception.custom.ValidationException;
import com.voluntarios.security.JwtTokenProvider;
import com.voluntarios.service.ActividadService;
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
@Tag(name = "Actividades", description = "Actividades de organizaciones")
public class ActividadRestController {
    private final ActividadService actividadService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "Obtener todas las actividades", responses = {
            @ApiResponse(description = "Operación exitosa", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Actividad.class)))) })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/actividad")
    public ResponseEntity<List<Actividad>> indexActividad() {
        List<Actividad> actividades = this.actividadService.findAll();
        return new ResponseEntity<>(actividades, HttpStatus.OK);
    }

    @Operation(summary = "Obtener actividades con paginación", responses = {
            @ApiResponse(description = "Operación exitosa", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Actividad.class)))) })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/actividad/page/{page}")
    public ResponseEntity<Page<Actividad>> indexPageActividad(@PathVariable Integer page) {
        Page<Actividad> actividades = this.actividadService.findAll(PageRequest.of(page, 4));
        return new ResponseEntity<>(actividades, HttpStatus.OK);
    }

    @Operation(summary = "Obtener actividades por usuario suscrito", responses = {
            @ApiResponse(description = "Operación exitosa", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Actividad.class)))) })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/actividad/user/{id}")
    public ResponseEntity<List<Actividad>> indexActividadByUser(@PathVariable Long id) {
        List<Actividad> actividades = this.actividadService.findByVoluntario(id);
        return new ResponseEntity<>(actividades, HttpStatus.OK);
    }

    @Operation(summary = "Obtener actividades por usuario autor", responses = {
            @ApiResponse(description = "Operación exitosa", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Actividad.class)))) })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/actividad/org/{id}")
    public ResponseEntity<List<Actividad>> indexActividadByOrg(@PathVariable Long id) {
        List<Actividad> actividades = this.actividadService.findByCreadoPor(id);
        return new ResponseEntity<>(actividades, HttpStatus.OK);
    }

    @Operation(summary = "Obtener una actividad", responses = {
            @ApiResponse(description = "Operación exitosa", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Actividad.class))),
            @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))) })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/actividad/{id}")
    public ResponseEntity<Actividad> showActividad(@PathVariable Long id) {
        Actividad actividad = this.actividadService.findById(id);
        return new ResponseEntity<>(actividad, HttpStatus.OK);
    }

    @Operation(summary = "Registrar una actividad", security = {
            @SecurityRequirement(name = "bearerAuth") }, responses = {
            @ApiResponse(description = "Operación exitosa", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Actividad.class))),
            @ApiResponse(responseCode = "400", description = "Requisición incorrecta", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = HttpResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Falla de autenticación", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = HttpResponse.class)))) })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/actividad")
    public ResponseEntity<?> createActividad(@Valid @RequestBody Actividad actividad, BindingResult result) throws ValidationException {
        Actividad newactividad = this.actividadService.save(actividad, result);
        return new ResponseEntity<>(newactividad, HttpStatus.OK);
    }

    @Operation(summary = "Alterar una actividad", security = {
            @SecurityRequirement(name = "bearerAuth") }, responses = {
            @ApiResponse(description = "Operación exitosa", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Actividad.class))),
            @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisición incorrecta", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "401", description = "Falla de autenticación", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))) })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PutMapping("/actividad/{id}")
    public ResponseEntity<Actividad> updateActividad(@PathVariable Long id, @Valid @RequestBody Actividad actividad,
                                            BindingResult result) throws ValidationException, MessagingException, TemplateException, IOException {
        Actividad updatedActividad = this.actividadService.update(id, actividad, result);
        return new ResponseEntity<>(updatedActividad, HttpStatus.CREATED);
    }

    @Operation(summary = "Suscribirse o Desuscribirse de una actividad", security = {
            @SecurityRequirement(name = "bearerAuth") }, responses = {
            @ApiResponse(description = "Operación exitosa", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Actividad.class))),
            @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisición incorrecta", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "401", description = "Falla de autenticación", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))) })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PutMapping("/actividad/{id}/suscribe")
    public ResponseEntity<Actividad> suscribeActividad(@PathVariable Long id, @RequestHeader(value = "Authorization", required = true) String authorization ) throws MessagingException, TemplateException, IOException {

        // Obtiene el username a partir del token de autenticacion
        String token = authorization.substring("Bearer ".length());
        String username = this.jwtTokenProvider.getSubject(token);

        Actividad updatedActividad = this.actividadService.suscribe(id, username);
        return new ResponseEntity<>(updatedActividad, HttpStatus.CREATED);
    }





}
