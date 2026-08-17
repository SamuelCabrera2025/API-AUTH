package apiAuth.authentication.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desactivamos CSRF (Cross-Site Request Forgery) porque usaremos Tokens JWT, no cookies de sesión
                .csrf(csrf -> csrf.disable())
                // 2. Indicamos que nuestra API no guardará el estado de las sesiones (Stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 3. Configuramos las rutas públicas y privadas
                .authorizeHttpRequests(auth -> auth
                        // Permitimos que cualquiera acceda a las rutas que empiecen con /auth (ej. /auth/login)
                        .requestMatchers("/auth/**").permitAll()
                        // Cualquier otra ruta requerirá que el usuario esté autenticado con su token
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    // Este Bean le enseña a Spring cómo encriptar y comparar contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Este Bean es el "Gerente de Autenticación", lo usaremos en nuestro AuthService para validar el login
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
