package apiAuth.authentication.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorDTO> manejarEstado(ResponseStatusException exception) {
        HttpStatus status = HttpStatus.valueOf(exception.getStatusCode().value());
        return crearRespuesta(status, exception.getReason());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> manejarValidacion(
            MethodArgumentNotValidException exception
    ) {
        String mensaje = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining(" "));

        return crearRespuesta(HttpStatus.BAD_REQUEST, mensaje);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorDTO> manejarJsonInvalido() {
        return crearRespuesta(
                HttpStatus.BAD_REQUEST,
                "El cuerpo de la petición no contiene un JSON válido."
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> manejarErrorInesperado(Exception exception) {
        log.error("Error inesperado en API-AUTH", exception);
        return crearRespuesta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error interno durante la autenticación."
        );
    }

    private ResponseEntity<ApiErrorDTO> crearRespuesta(HttpStatus status, String mensaje) {
        ApiErrorDTO error = new ApiErrorDTO(
                false,
                status.value(),
                mensaje,
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(error);
    }
}
