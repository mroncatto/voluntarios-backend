package com.voluntarios.config;

import lombok.Data;

import java.util.Date;

@Data
public class AuthResponse {
    private String access_token;
    private String refresh_token;
    private Date expires_at;
    private String token_type;
}
