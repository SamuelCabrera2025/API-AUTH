package apiAuth.authentication.security;

import apiAuth.authentication.authentication.models.entity.AuthEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    private final Key signingKey;
    private final long expirationMs;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expirationMs
    ) {
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            throw new IllegalStateException(
                    "JWT_SECRET debe contener al menos 32 caracteres."
            );
        }

        this.signingKey = Keys.hmacShaKeyFor(secretBytes);
        this.expirationMs = expirationMs;
    }

    public String generarToken(AuthEntity usuario) {
        Instant ahora = Instant.now();

        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("idUsuario", usuario.getId())
                .claim("rol", usuario.getRol())
                .setIssuedAt(Date.from(ahora))
                .setExpiration(Date.from(ahora.plusMillis(expirationMs)))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public long getExpirationSeconds() {
        return expirationMs / 1000;
    }
}
