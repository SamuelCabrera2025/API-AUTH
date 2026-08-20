package apiAuth.authentication.authentication.controller;

import apiAuth.authentication.authentication.models.entity.AuthEntity;
import apiAuth.authentication.authentication.repository.AuthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void crearUsuario() {
        authRepository.deleteAll();
        authRepository.save(new AuthEntity(
                null,
                "padre.prueba@ricaldone.edu.sv",
                passwordEncoder.encode("Prueba123*"),
                "PADRE"
        ));
    }

    @Test
    void endpointLoginEntregaUnJwt() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "padre.prueba@ricaldone.edu.sv",
                                  "password": "Prueba123*"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").isNumber())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.tipoToken").value("Bearer"))
                .andExpect(jsonPath("$.rol").value("PADRE"));
    }

    @Test
    void endpointLoginRechazaCredencialesIncorrectas() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "padre.prueba@ricaldone.edu.sv",
                                  "password": "Incorrecta"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.exitoso").value(false))
                .andExpect(jsonPath("$.mensaje")
                        .value("El correo o la contraseña son incorrectos."));
    }
}
