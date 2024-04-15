package co.finanplus.api.domain.Gastos.Tarjetas;

import java.time.LocalDate;
import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GastoTarjetaRepository extends JpaRepository<GastoTarjeta, Long> {
    List<GastoTarjeta> findByTarjetaCredito_TarjetaCreditoID(Long tarjetaCreditoID);
    List<GastoTarjeta> findByTarjetaCredito_TarjetaCreditoIDAndTarjetaCredito_UsuarioID(Long tarjetaCreditoID, String usuarioID);
    List<GastoTarjeta> findByTarjetaCredito_TarjetaCreditoIDAndTarjetaCredito_UsuarioIDAndFechaBetween(
        Long tarjetaCreditoID, 
        String usuarioID,
        LocalDate startDate, 
        LocalDate endDate
    );
}
