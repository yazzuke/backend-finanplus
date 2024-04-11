package co.finanplus.api.domain.Gastos.Fijos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GastoFijoRepository extends JpaRepository<GastoFijo, Long>             {
    List<GastoFijo> findByUsuarioID(String usuarioID);
}


