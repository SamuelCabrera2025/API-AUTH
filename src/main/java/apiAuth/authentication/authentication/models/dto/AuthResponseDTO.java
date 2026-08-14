package apiAuth.authentication.authentication.models.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponseDTO {

    private Long idUsuario;
    private String token; // Aquí enviaremos el JWT
    private String rol;// Útil para que el frontend sepa qué menú mostrar (docente o estudiante)
}
