package apiAuth.authentication.authentication.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Table(name = "USUARIOS")
@Setter
@NotBlank
public class AuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long id;
    @Column(name = "USU_EMAIL")
    private String username;
    @Column(name ="USU_PASSWORD", nullable = false)
    private String password;
    @Column(name ="USU_ROL")
    private String rol;
}
