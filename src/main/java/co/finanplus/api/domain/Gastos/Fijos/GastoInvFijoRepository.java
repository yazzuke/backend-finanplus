package co.finanplus.api.domain.Gastos.Fijos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GastoInvFijoRepository extends JpaRepository<GastoInvFijo, Long> {
    List<GastoInvFijo> findByGastoFijo_GastoFijoID(Long gastoFijoID);
    //List<GastoInvFijo> findByGastoFijo_GastoFijoIDAndGastoFijo_UsuarioID(Long gastoFijoID, String usuarioID);

    List<GastoInvFijo> findByGastoFijo_GastoFijoIDAndGastoFijo_UsuarioIDAndFechaInsertadoBetween(
        Long gastoFijoID, 
        String usuarioID,
        LocalDate startDate, 
        LocalDate endDate
    );
}




