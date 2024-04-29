package co.finanplus.api.domain.ResumenMensual;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.finanplus.api.dto.TotalesMensualesDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResumenMensualRepository extends JpaRepository<ResumenMensual, Long> {
    List<ResumenMensual> findByUsuarioID(String usuarioID);

    Optional<ResumenMensual> findByUsuarioIDAndFechaInicio(String usuarioID, LocalDate fechaInicio);

    List<ResumenMensual> findByUsuarioIDAndFechaInicioBetween(String usuarioID, LocalDate start, LocalDate end);

    List<ResumenMensual> findByFechaInicioAndCerrado(LocalDate fechaInicio, Boolean cerrado);

    @Query("SELECT new co.finanplus.api.dto.TotalesMensualesDTO(FUNCTION('YEAR', r.fechaInicio), FUNCTION('MONTH', r.fechaInicio), SUM(r.totalIngresos), SUM(r.totalGastos)) "
            +
            "FROM ResumenMensual r WHERE r.usuarioID = :usuarioID GROUP BY FUNCTION('YEAR', r.fechaInicio), FUNCTION('MONTH', r.fechaInicio)")
    List<TotalesMensualesDTO> findTotalesByUsuarioIDGroupedByMonth(@Param("usuarioID") String usuarioID);
    
    
    @Query("SELECT new co.finanplus.api.dto.TotalesMensualesDTO(FUNCTION('YEAR', r.fechaInicio), FUNCTION('MONTH', r.fechaInicio), SUM(r.totalIngresos), SUM(r.totalGastos)) " +
       "FROM ResumenMensual r WHERE r.usuarioID = :usuarioID " +
       "AND (:year IS NULL OR FUNCTION('YEAR', r.fechaInicio) = :year) " +
       "GROUP BY FUNCTION('YEAR', r.fechaInicio), FUNCTION('MONTH', r.fechaInicio)")
List<TotalesMensualesDTO> findTotalesByUsuarioIDAndYear(@Param("usuarioID") String usuarioID, @Param("year") Integer year);


}