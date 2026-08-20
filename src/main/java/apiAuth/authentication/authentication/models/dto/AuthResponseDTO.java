package apiAuth.authentication.authentication.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AuthResponseDTO {

    private Long idUsuario;
    private String token;
    private String tipoToken;
    private String rol;
    private long expiraEnSegundos;
}
