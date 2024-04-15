package co.finanplus.api.domain.Gastos.Tarjetas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TarjetaCreditoRepository extends JpaRepository<TarjetaCredito, Long> {
    List<TarjetaCredito> findByUsuarioID(String usuarioID);
    List<TarjetaCredito> findByUsuarioIDAndFechaBetween(String usuarioID, LocalDate start, LocalDate end);
}