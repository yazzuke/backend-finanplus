package co.finanplus.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import co.finanplus.api.domain.Gastos.Tarjetas.GastoTarjeta;
import co.finanplus.api.domain.Gastos.Tarjetas.GastoTarjetaRepository;
import co.finanplus.api.domain.Gastos.Tarjetas.TarjetaCredito;
import co.finanplus.api.domain.Gastos.Tarjetas.TarjetaCreditoRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class TarjetaCreditoService {
    
    @Autowired
    private TarjetaCreditoRepository tarjetaCreditoRepository;

    @Autowired
    private GastoTarjetaRepository gastoTarjetaRepository;

    @Scheduled(cron = "0 0 0 1 * ?")
    public void actualizarCuotas() {
        List<TarjetaCredito> tarjetas = tarjetaCreditoRepository.findAll();
    
        for (TarjetaCredito tarjeta : tarjetas) {
            List<GastoTarjeta> gastos = tarjeta.getGastos();
            for (GastoTarjeta gasto : gastos) {
                // Actualizar cuota actual y valor cuota
                gasto.incrementarCuotaActual();
                BigDecimal valorCuota = gasto.getValorTotalGasto().divide(BigDecimal.valueOf(gasto.getCuotaTotal()), 2, RoundingMode.HALF_UP);
                gasto.setValorCuotaGasto(valorCuota);
    
                // Guardar cambios
                gastoTarjetaRepository.save(gasto);
            }
        }
    }
    
}