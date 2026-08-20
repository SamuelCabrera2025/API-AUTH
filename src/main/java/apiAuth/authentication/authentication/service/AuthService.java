package apiAuth.authentication.authentication.service;

import apiAuth.authentication.authentication.models.dto.AuthRequestDTO;
import apiAuth.authentication.authentication.models.dto.AuthResponseDTO;
import apiAuth.authentication.authentication.models.entity.AuthEntity;
import apiAuth.authentication.authentication.repository.AuthRepository;
import apiAuth.authentication.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponseDTO login(AuthRequestDTO request) {
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);

        AuthEntity usuario = authRepository.findByEmailIgnoreCase(email)
                .orElseThrow(this::credencialesInvalidas);

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw credencialesInvalidas();
        }

        return AuthResponseDTO.builder()
                .idUsuario(usuario.getId())
                .token(jwtService.generarToken(usuario))
                .tipoToken("Bearer")
                .rol(usuario.getRol())
                .expiraEnSegundos(jwtService.getExpirationSeconds())
                .build();
    }

    private ResponseStatusException credencialesInvalidas() {
        // El mensaje es deliberadamente genérico: no revela si falló el correo o la clave.
        return new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "El correo o la contraseña son incorrectos."
        );
    }
}
