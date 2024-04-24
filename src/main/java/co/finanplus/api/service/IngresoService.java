package co.finanplus.api.service;

import java.math.BigDecimal;
import co.finanplus.api.domain.Ingresos.Ingreso;
import co.finanplus.api.domain.Ingresos.IngresoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class IngresoService {

    private final IngresoRepository ingresoRepository;
    private final ResumenMensualService resumenMensualService;

    public IngresoService(IngresoRepository ingresoRepository, ResumenMensualService resumenMensualService) {
        this.ingresoRepository = ingresoRepository;
        this.resumenMensualService = resumenMensualService;
    }

    public Ingreso agregarIngreso(Ingreso ingreso) {
        ingreso = ingresoRepository.save(ingreso);
        resumenMensualService.actualizarResumenMensualConIngresos(ingreso.getUsuarioID(), ingreso.getFecha());
        return ingreso;
    }

    public Ingreso actualizarIngreso(Long ingresoID, Ingreso ingresoDetails) {
        return ingresoRepository.findById(ingresoID).map(ingreso -> {
            ingreso.setConcepto(ingresoDetails.getConcepto());
            ingreso.setMonto(ingresoDetails.getMonto());
            Ingreso updatedIngreso = ingresoRepository.save(ingreso);
            resumenMensualService.actualizarResumenMensualConIngresos(updatedIngreso.getUsuarioID(),
                    updatedIngreso.getFecha());
            return updatedIngreso;
        }).orElse(null);
    }

    public boolean eliminarIngreso(Long ingresoID) {
        return ingresoRepository.findById(ingresoID).map(ingreso -> {
            ingresoRepository.delete(ingreso);
            resumenMensualService.actualizarResumenMensualConIngresos(ingreso.getUsuarioID(), ingreso.getFecha());
            return true;
        }).orElse(false);
    }

}