    package co.finanplus.api.domain.Gastos.Fijos;

    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.time.LocalDate;
    import java.util.List;

    @Repository
    public interface GastoFijoRepository extends JpaRepository<GastoFijo, Long>             {
        List<GastoFijo> findByUsuarioID(String usuarioID);
        List<GastoFijo> findByUsuarioIDAndFechaBetween(String usuarioID, LocalDate start, LocalDate end);
        List<GastoFijo> findByUsuarioIDOrderByFecha(String usuarioID);

        
    }


