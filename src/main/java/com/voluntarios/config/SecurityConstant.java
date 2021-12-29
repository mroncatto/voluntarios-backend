package com.voluntarios.config;

public class SecurityConstant {
    public static final String TOKEN_CANNOT_BE_VERIFIED = "El token no pudo ser validado";
    public static final String COMPANY_NAME = "Red de Voluntarios";
    public static final String COMPANY_INFO = "Red de voluntarios para organizaciones registrar actividades en busca de voluntarios";
    public static final String FORBIDDEN_MESSAGE = "Debes autenticarse para acceder a esta pagina";
    public static final String ACCESS_DENIED_MESSAGE = "No posees acceso suficiente para acceder a esta pagina";
    public static final String[] PUBLIC_URLS = {"/swagger-ui/**", "/v3/api-docs/**", "/" };
    public static final String[] ALLOW_POST = {"/v1/user"};
    public static final String[] ALLOW_GET = {"/v1/actividad/**", "/v1/user/**"};
    public static final String[] ALLOWED_ORIGINS = {"https://voluntarios-frontend.herokuapp.com", "http://localhost:4200"};
}
