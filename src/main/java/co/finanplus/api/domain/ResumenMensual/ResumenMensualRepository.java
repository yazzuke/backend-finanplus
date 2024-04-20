package co.finanplus.api.domain.ResumenMensual;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface  ResumenMensualRepository extends JpaRepository<ResumenMensual, Long>{
    List<ResumenMensual> findByUsuarioID(String usuarioID);
    List<ResumenMensual> findByUsuarioIDAndFechaInicioBetween(String usuarioID, LocalDate start, LocalDate end);
}
