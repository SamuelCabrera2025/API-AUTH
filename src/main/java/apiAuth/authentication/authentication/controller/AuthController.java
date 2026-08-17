package apiAuth.authentication.authentication.controller;

import apiAuth.authentication.authentication.models.dto.AuthRequestDTO;
import apiAuth.authentication.authentication.models.dto.AuthResponseDTO;
import apiAuth.authentication.authentication.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // Inyectamos nuestro servicio
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Ruta final: POST http://localhost:8080/auth/login
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        // Llamamos al método login que creamos en el AuthService
        AuthResponseDTO response = authService.login(request);

        // Devolvemos un código 200 OK con el Token y el Rol adentro
        return ResponseEntity.ok(response);
    }
}
