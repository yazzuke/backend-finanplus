package co.finanplus.api.domain.Gastos.Fijos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GastoInvFijoRepository extends JpaRepository<GastoInvFijo, Long>{
    List<GastoInvFijo> findByGastoFijo_GastoFijoID(Long gastoFijoID);
}





