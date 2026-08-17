package apiAuth.authentication.authentication.repository;


import apiAuth.authentication.authentication.models.entity.AuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface AuthRepository extends JpaRepository<AuthEntity,Long> {

     boolean existsByUsernameAndPassword(String username, String Password);

     // Este es el método que tu AuthService está buscando a gritos:
     Optional<AuthEntity> findByUsername(String username);
}
