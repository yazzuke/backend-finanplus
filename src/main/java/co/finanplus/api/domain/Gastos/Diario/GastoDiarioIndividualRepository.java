package co.finanplus.api.domain.Gastos.Diario;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GastoDiarioIndividualRepository extends JpaRepository<GastoDiarioIndividual, Long>   {
    List<GastoDiarioIndividual> findByGastoDiario_GastoDiarioID(Long gastoDiarioID);
    //List<GastoDiarioIndividual> findByGastoDiario_GastoDiarioIDAndGastoDiario_UsuarioID(Long gastoDiarioID, String usuarioID);

    List<GastoDiarioIndividual> findByGastoDiario_GastoDiarioIDAndGastoDiario_UsuarioIDAndFechaInsertadoBetween(
        Long gastoDiarioID, 
        String usuarioID,
        LocalDate startDate, 
        LocalDate endDate
    );


}
