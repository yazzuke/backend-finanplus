package co.finanplus.api.domain.Ahorros;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AhorroRepository extends JpaRepository<Ahorro, Long> {
    List<Ahorro> findByUsuarioID(String usuarioID);
}


