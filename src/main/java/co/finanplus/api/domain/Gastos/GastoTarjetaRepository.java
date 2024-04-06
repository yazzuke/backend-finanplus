package co.finanplus.api.domain.Gastos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GastoTarjetaRepository extends JpaRepository<GastoTarjeta, Long> {

}
