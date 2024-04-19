package co.finanplus.api.domain.Gastos.Diario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GastoDiarioRepository extends JpaRepository<GastoDiario, Long>{
    List<GastoDiario> findByUsuarioID(String usuarioID);
    List<GastoDiario> findByUsuarioIDAndFechaBetween(String usuarioID, LocalDate start, LocalDate end);


    
}
