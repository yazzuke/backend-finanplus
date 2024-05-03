package co.finanplus.api.domain.Gastos.Tarjetas;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



import java.time.LocalDate;
import java.util.List;

@Repository
public interface TarjetaCreditoRepository extends JpaRepository<TarjetaCredito, Long> {

    @Query("SELECT t FROM TarjetaCredito t WHERE t.usuarioID = :usuarioId AND t.tarjetaCreditoID = :tarjetaCreditoId")
    TarjetaCredito findByUsuarioIdAndTarjetaCreditoId(@Param("usuarioId") String usuarioId, @Param("tarjetaCreditoId") Long tarjetaCreditoId);
    
    @Cacheable(value = "tarjetasCreditoPorUsuario", key = "#usuarioId")
    List<TarjetaCredito> findByUsuarioID(String usuarioID);
    List<TarjetaCredito> findByUsuarioIDAndFechaBetween(String usuarioID, LocalDate start, LocalDate end);

    List<TarjetaCredito> findByUsuarioIDOrderByFecha(String usuarioID);
}