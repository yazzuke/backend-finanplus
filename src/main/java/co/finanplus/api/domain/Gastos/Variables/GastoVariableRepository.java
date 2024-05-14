package co.finanplus.api.domain.Gastos.Variables;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;

@Repository
public interface GastoVariableRepository extends JpaRepository<GastoVariable, Long> {
    List<GastoVariable> findByUsuarioID(String usuarioID);

    List<GastoVariable> findByUsuarioIDAndFechaBetween(String usuarioID, LocalDate start, LocalDate end);

    List<GastoVariable> findByUsuarioIDOrderByFecha(String usuarioID);

}
