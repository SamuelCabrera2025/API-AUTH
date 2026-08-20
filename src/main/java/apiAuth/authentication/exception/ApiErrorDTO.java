package apiAuth.authentication.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApiErrorDTO {

    private boolean exitoso;
    private int estado;
    private String mensaje;
    private LocalDateTime fechaHora;
}
