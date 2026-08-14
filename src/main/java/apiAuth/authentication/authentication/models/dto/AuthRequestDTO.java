package apiAuth.authentication.authentication.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NotBlank
public class AuthRequestDTO {

    private String email;
    private String password;

}
