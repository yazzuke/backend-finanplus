package co.finanplus.api.domain.usuarios;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario, String> {
   // Método para buscar un usuario por su email
    Optional<Usuario> findByEmail(String email);
    }
