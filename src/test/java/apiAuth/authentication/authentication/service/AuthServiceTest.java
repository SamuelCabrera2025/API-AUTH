package apiAuth.authentication.authentication.service;

import apiAuth.authentication.authentication.models.dto.AuthRequestDTO;
import apiAuth.authentication.authentication.models.dto.AuthResponseDTO;
import apiAuth.authentication.authentication.models.entity.AuthEntity;
import apiAuth.authentication.authentication.repository.AuthRepository;
import apiAuth.authentication.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private JwtService jwtService;

    private AuthService authService;
    private AuthEntity usuario;

    @BeforeEach
    void preparar() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthService(authRepository, passwordEncoder, jwtService);

        usuario = new AuthEntity(
                7L,
                "docente.prueba@ricaldone.edu.sv",
                passwordEncoder.encode("Prueba123*"),
                "DOCENTE ACADÉMICO"
        );
    }

    @Test
    void loginCorrectoDevuelveTokenRolEId() {
        AuthRequestDTO request = crearRequest("Prueba123*");
        when(authRepository.findByEmailIgnoreCase(request.getEmail()))
                .thenReturn(Optional.of(usuario));
        when(jwtService.generarToken(usuario)).thenReturn("jwt-de-prueba");
        when(jwtService.getExpirationSeconds()).thenReturn(3600L);

        AuthResponseDTO response = authService.login(request);

        assertEquals(7L, response.getIdUsuario());
        assertEquals("jwt-de-prueba", response.getToken());
        assertEquals("Bearer", response.getTipoToken());
        assertEquals("DOCENTE ACADÉMICO", response.getRol());
        assertEquals(3600L, response.getExpiraEnSegundos());
    }

    @Test
    void loginRechazaUnaPasswordIncorrecta() {
        AuthRequestDTO request = crearRequest("ClaveIncorrecta");
        when(authRepository.findByEmailIgnoreCase(request.getEmail()))
                .thenReturn(Optional.of(usuario));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> authService.login(request)
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    @Test
    void loginNoRevelaSiElCorreoNoExiste() {
        AuthRequestDTO request = crearRequest("Prueba123*");
        when(authRepository.findByEmailIgnoreCase(request.getEmail()))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> authService.login(request)
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals(
                "El correo o la contraseña son incorrectos.",
                exception.getReason()
        );
    }

    private AuthRequestDTO crearRequest(String password) {
        AuthRequestDTO request = new AuthRequestDTO();
        request.setEmail("docente.prueba@ricaldone.edu.sv");
        request.setPassword(password);
        return request;
    }
}
