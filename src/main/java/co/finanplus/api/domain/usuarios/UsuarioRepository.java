package co.finanplus.api.domain.usuarios;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    }
