package co.finanplus.api.domain.Gastos.Tarjetas;

import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GastoTarjetaRepository extends JpaRepository<GastoTarjeta, Long> {
    List<GastoTarjeta> findByTarjetaCredito_TarjetaCreditoID(Long tarjetaCreditoID); 
}
