package co.finanplus.api.domain.ResumenMensual;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResumenMensualRepository extends JpaRepository<ResumenMensual, Long>{
    List<ResumenMensual> findByUsuarioID(String usuarioID);
    Optional<ResumenMensual> findByUsuarioIDAndFechaInicio(String usuarioID, LocalDate fechaInicio);
    List<ResumenMensual> findByUsuarioIDAndFechaInicioBetween(String usuarioID, LocalDate start, LocalDate end);
    List<ResumenMensual> findByFechaInicioAndCerrado(LocalDate fechaInicio, Boolean cerrado);
}