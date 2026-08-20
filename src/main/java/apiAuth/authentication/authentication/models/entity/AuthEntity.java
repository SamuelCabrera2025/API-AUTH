package apiAuth.authentication.authentication.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USUARIOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long id;

    @Column(name = "USU_EMAIL", nullable = false, length = 100)
    private String email;

    @Column(name = "USU_PASSWORD", nullable = false, length = 255)
    private String password;

    @Column(name = "USU_ROL", nullable = false, length = 255)
    private String rol;
}
