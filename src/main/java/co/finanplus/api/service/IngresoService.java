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

 
}