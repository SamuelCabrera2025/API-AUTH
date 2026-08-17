package apiAuth.authentication.authentication.service;

import apiAuth.authentication.authentication.models.entity.AuthEntity;
import apiAuth.authentication.authentication.models.dto.AuthRequestDTO;
import apiAuth.authentication.authentication.models.dto.AuthResponseDTO;
import apiAuth.authentication.authentication.repository.AuthRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    // 1. Inyectamos el Repositorio y el Manejador de Autenticación
    private final AuthRepository authRepository;
    private final AuthenticationManager authenticationManager;

    public AuthService(AuthRepository authRepository, AuthenticationManager authenticationManager) {
        this.authRepository = authRepository;
        this.authenticationManager = authenticationManager;
    }

    // 2. Tu llave secreta
    private static final String SECRET_KEY = "QWxhZGRpbjpvcGVuIHNlc2FtZV9hbGFkZGluX29wZW5fc2VzYW1l";

    // 3. NUEVO: El método que hace el inicio de sesión
    public AuthResponseDTO login(AuthRequestDTO request) {

        // A) Validamos contraseña (Asegúrate de que tu AuthRequestDTO use getUsername o getEmail según lo tengas)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // B) Buscamos en la base de datos
        AuthEntity usuario = authRepository.findByUsername(request.getEmail()) // O findByEmail, según lo tengas en tu Repositorio
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // C) Usamos tu método de abajo para crear el token
        String tokenGenerado = generarToken(usuario);

        // D) Devolvemos la respuesta al Controlador
        return new AuthResponseDTO(tokenGenerado, usuario.getRol());
    }

    // 4. LO QUE YA TENÍAS: Método para crear el token
    public String generarToken(AuthEntity usuario) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("rol", usuario.getRol());

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(usuario.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 5. LO QUE YA TENÍAS: Método interno para la llave secreta
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

