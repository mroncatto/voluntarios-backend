package com.voluntarios.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC256;
import static com.voluntarios.config.AppConstant.APP_NAME;
import static java.util.Arrays.stream;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expires}")
    private int expires;

    @Value("${jwt.refresh}")
    private int refresh;

    @Value("${app.timezone}")
    private String timezone;

    public String generateJwtToken(UserPrincipal user, boolean refresh) {
        Algorithm algorithm = HMAC256(secret.getBytes());
        return JWT.create()
                .withAudience(APP_NAME)
                .withSubject(user.getUsername())
                .withIssuedAt(this.getDate())
                .withExpiresAt(this.getDate(refresh ? this.refresh : this.expires))
                .withIssuer(ServletUriComponentsBuilder.fromCurrentContextPath().path("").toUriString())
                .withClaim("roles", refresh ? null : user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public String getSubject(String token) {
        return getDecodedJwt(token).getSubject();
    }

    private DecodedJWT getDecodedJwt(String token) {
        Algorithm algorithm = HMAC256(secret.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public Collection<SimpleGrantedAuthority> getAuthorities(String token) {
        String[] claims = getClaimsFromToken(token);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(claims).forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });
        return authorities;

    }

    public String[] getClaimsFromToken(String token) {
        return getDecodedJwt(token).getClaim("roles").asArray(String.class);
    }

    // Obtener fecha desde el calendario
    private Date getDate() {
        return Calendar.getInstance(TimeZone.getTimeZone(this.timezone)).getTime();
    }

    // Obtener fecha desde el calendario con el incremento de tiempo definido para expirar el token
    private Date getDate(int increase) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(this.timezone));
        calendar.add(Calendar.MILLISECOND, increase);
        return calendar.getTime();
    }
}
