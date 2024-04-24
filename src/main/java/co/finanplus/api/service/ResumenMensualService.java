package co.finanplus.api.service;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import co.finanplus.api.domain.Ingresos.IngresoRepository;
import co.finanplus.api.domain.ResumenMensual.ResumenMensual;
import co.finanplus.api.domain.ResumenMensual.ResumenMensualRepository;
import co.finanplus.api.domain.usuarios.Usuario;
import co.finanplus.api.domain.usuarios.UsuarioRepository;
import co.finanplus.api.domain.Ingresos.Ingreso;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ResumenMensualService {

    private final ResumenMensualRepository resumenMensualRepository;
    private final UsuarioRepository usuarioRepository;
    private final IngresoRepository ingresoRepository;


    public ResumenMensualService(ResumenMensualRepository resumenMensualRepository, UsuarioRepository usuarioRepository, IngresoRepository ingresoRepository) {
        this.resumenMensualRepository = resumenMensualRepository;
        this.usuarioRepository = usuarioRepository;
        this.ingresoRepository = ingresoRepository;
    }


    public ResumenMensual crearResumenInicial(String usuarioID, LocalDate fechaInicio) {
        ResumenMensual resumen = new ResumenMensual(
            null,
            usuarioID,
            fechaInicio,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            false
        );
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
        List<ResumenMensual> resumenesDelMesPasado = resumenMensualRepository.findByFechaInicioAndCerrado(primerDiaDelMesActual, false);
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

  
}   