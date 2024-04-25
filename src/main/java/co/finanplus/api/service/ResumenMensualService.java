package co.finanplus.api.service;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import co.finanplus.api.domain.Ingresos.IngresoRepository;
import co.finanplus.api.domain.ResumenMensual.ResumenMensual;
import co.finanplus.api.domain.ResumenMensual.ResumenMensualRepository;
import co.finanplus.api.domain.usuarios.Usuario;
import co.finanplus.api.domain.usuarios.UsuarioRepository;
import co.finanplus.api.domain.Gastos.Tarjetas.TarjetaCredito;
import co.finanplus.api.domain.Gastos.Tarjetas.TarjetaCreditoRepository;
import co.finanplus.api.domain.Ingresos.Ingreso;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ResumenMensualService {

    private final ResumenMensualRepository resumenMensualRepository;
    private final UsuarioRepository usuarioRepository;
    private final IngresoRepository ingresoRepository;
    private final TarjetaCreditoRepository tarjetaCreditoRepository;

    public ResumenMensualService(ResumenMensualRepository resumenMensualRepository, UsuarioRepository usuarioRepository,
            IngresoRepository ingresoRepository, TarjetaCreditoRepository tarjetaCreditoRepository) {
        this.resumenMensualRepository = resumenMensualRepository;
        this.usuarioRepository = usuarioRepository;
        this.ingresoRepository = ingresoRepository;
        this.tarjetaCreditoRepository = tarjetaCreditoRepository;
    }

    public ResumenMensual crearResumenInicial(String usuarioID, LocalDate fechaInicio) {
        ResumenMensual resumen = new ResumenMensual(
                null,
                usuarioID,
                fechaInicio,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                false);
        return resumenMensualRepository.save(resumen);
    }

    // Cada vez que se inicia un nuevo mes, se cierran los resúmenes del mes pasado
    // y se crean nuevos resúmenes para el mes actual
    @Scheduled(cron = "0 0 0 1 * ?")
    public void cerrarYCrearResumenes() {
        LocalDate hoy = LocalDate.now();
        LocalDate primerDiaDelMesActual = hoy.withDayOfMonth(1);
        LocalDate primerDiaDelProximoMes = hoy.plusMonths(1).withDayOfMonth(1);

        // Cerrar los resúmenes del mes pasado
        List<ResumenMensual> resumenesDelMesPasado = resumenMensualRepository
                .findByFechaInicioAndCerrado(primerDiaDelMesActual, false);
        for (ResumenMensual resumen : resumenesDelMesPasado) {
            resumen.setCerrado(true);
            resumenMensualRepository.save(resumen);
        }

        // Crear nuevos resúmenes para el próximo mes
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario usuario : usuarios) {
            crearResumenInicial(usuario.getId(), primerDiaDelProximoMes);
        }
    }

    // Método para calcular el total de ingresos de un usuario en un mes específico
    public BigDecimal calcularTotalIngresosPorUsuarioYFecha(String usuarioID, LocalDate fechaInicio) {
        LocalDate fechaFin = fechaInicio.withDayOfMonth(fechaInicio.lengthOfMonth()); // Último día del mes
        List<Ingreso> ingresosDelUsuario = ingresoRepository.findByUsuarioIDAndFechaBetween(usuarioID, fechaInicio,
                fechaFin);
        return ingresosDelUsuario.stream()  
                .map(Ingreso::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    

    public ResumenMensual actualizarResumenMensualConIngresos(String usuarioID, LocalDate fechaInicio) {
        BigDecimal totalIngresos = calcularTotalIngresosPorUsuarioYFecha(usuarioID, fechaInicio);

        Optional<ResumenMensual> optionalResumen = resumenMensualRepository.findByUsuarioIDAndFechaInicio(usuarioID,
                fechaInicio);
        ResumenMensual resumen;
        if (optionalResumen.isPresent()) {
            resumen = optionalResumen.get();
        } else {
            // Only create a new summary if one does not exist
            resumen = crearResumenInicial(usuarioID, fechaInicio);
        }

        resumen.setTotalIngresos(totalIngresos);
        return resumenMensualRepository.save(resumen);
    }

    public List<ResumenMensual> findByUsuarioID(String usuarioID) {
        return resumenMensualRepository.findByUsuarioID(usuarioID);
    }

    

}