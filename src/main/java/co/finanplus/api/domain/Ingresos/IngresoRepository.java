package co.finanplus.api.domain.Ingresos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IngresoRepository extends JpaRepository<Ingreso, Long> {
    List<Ingreso> findByUsuarioID(String usuarioID);
    List<Ingreso> findByUsuarioIDAndFechaBetween(String usuarioID, LocalDate start, LocalDate end);
}