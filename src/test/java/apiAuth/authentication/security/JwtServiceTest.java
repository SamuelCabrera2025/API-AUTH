package apiAuth.authentication.security;

import apiAuth.authentication.authentication.models.entity.AuthEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JwtServiceTest {

    private static final String SECRET =
            "clave-unitaria-con-mas-de-32-caracteres-seguros";

    @Test
    void tokenIncluyeCorreoIdYRol() {
        JwtService jwtService = new JwtService(SECRET, 3600000L);
        AuthEntity usuario = new AuthEntity(
                11L,
                "padre.prueba@ricaldone.edu.sv",
                "hash-no-utilizado-en-esta-prueba",
                "PADRE"
        );

        String token = jwtService.generarToken(usuario);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertNotNull(token);
        assertEquals(usuario.getEmail(), claims.getSubject());
        assertEquals(usuario.getId().intValue(), claims.get("idUsuario", Integer.class));
        assertEquals(usuario.getRol(), claims.get("rol", String.class));
    }
}
