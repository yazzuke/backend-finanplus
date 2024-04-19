package co.finanplus.api.domain.Gastos.Variables;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GastoVariableIndividualRepository extends JpaRepository<GastoVariableIndividual, Long>{
    List<GastoVariableIndividual> findByGastoVariable_GastoVariableID(Long gastoVariableID);
   // List<GastoVariableIndividual> findByGastoVariable_GastoVariableIDAndGastoVariable_UsuarioID(Long gastoVariableID, String usuarioID);

    List<GastoVariableIndividual> findByGastoVariable_GastoVariableIDAndGastoVariable_UsuarioIDAndFechaInsertadoBetween(
        Long gastoVariableID, 
        String usuarioID,
        LocalDate startDate, 
        LocalDate endDate
    );


}
