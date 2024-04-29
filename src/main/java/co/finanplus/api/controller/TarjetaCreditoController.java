package co.finanplus.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import co.finanplus.api.domain.Gastos.Tarjetas.GastoTarjeta;
import co.finanplus.api.domain.Gastos.Tarjetas.GastoTarjetaRepository;
import co.finanplus.api.domain.Gastos.Tarjetas.TarjetaCredito;
import co.finanplus.api.domain.Gastos.Tarjetas.TarjetaCreditoRepository;
import co.finanplus.api.domain.Gastos.Tarjetas.TipoGasto;
import co.finanplus.api.domain.Gastos.Tarjetas.TipoGastoRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/usuarios/{usuarioID}/tarjetascredito")
public class TarjetaCreditoController {

    @Autowired
    private TarjetaCreditoRepository tarjetaCreditoRepository;

    @Autowired
    private GastoTarjetaRepository gastoTarjetaRepository; // Asegúrate de tener esta dependencia inyectada

    // endpoint para obtener las tarjetas de crédito de un usuario
    @GetMapping
    public ResponseEntity<List<TarjetaCredito>> listTarjetas(@PathVariable String usuarioID) {
        List<TarjetaCredito> tarjetas = tarjetaCreditoRepository.findByUsuarioID(usuarioID);
        return new ResponseEntity<>(tarjetas, HttpStatus.OK);
    }

    // endpoint para agregar una tarjeta de crédito a un usuario
    @PostMapping
    public ResponseEntity<TarjetaCredito> addTarjeta(@PathVariable String usuarioID,
            @RequestBody TarjetaCredito tarjeta) {
        tarjeta.setUsuarioID(usuarioID);
        tarjeta.setFecha(LocalDate.now());
        TarjetaCredito savedTarjeta = tarjetaCreditoRepository.save(tarjeta);
        return new ResponseEntity<>(savedTarjeta, HttpStatus.CREATED);
    }

    // endpoint para agregar un gasto a una tarjeta de crédito específica
    @PostMapping("/{tarjetaCreditoID}/gastos")
    public ResponseEntity<GastoTarjeta> addGasto(@PathVariable Long tarjetaCreditoID,
            @RequestBody GastoTarjeta gasto) {
        TarjetaCredito tarjeta = tarjetaCreditoRepository.findById(tarjetaCreditoID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Tarjeta de Crédito no encontrada con ID: " + tarjetaCreditoID));
        gasto.setTarjetaCredito(tarjeta);
        gasto.setCuotaActual(1); // Inicializa la cuota actual en 1
        gasto.setFecha(LocalDate.now());

        if (gasto.getCuotaTotal() != null && gasto.getValorTotalGasto() != null) {
            BigDecimal cuota = gasto.getValorTotalGasto()
                    .divide(BigDecimal.valueOf(gasto.getCuotaTotal()), 2, RoundingMode.HALF_UP);
            gasto.setValorCuotaGasto(cuota);
        }

        // Agrega el valor del gasto al total actual de la tarjeta de crédito
        BigDecimal valorTotalActual = tarjeta.getValorTotal();
        if (valorTotalActual == null) {
            valorTotalActual = BigDecimal.ZERO;
        }
        BigDecimal nuevoValorTotal = valorTotalActual.add(gasto.getValorTotalGasto());
        tarjeta.setValorTotal(nuevoValorTotal);

        // Guarda el gasto y actualiza la tarjeta de crédito
        GastoTarjeta savedGasto = gastoTarjetaRepository.save(gasto);

        tarjetaCreditoRepository.save(tarjeta); // Guarda la tarjeta de crédito con el nuevo valor total

        return new ResponseEntity<>(savedGasto, HttpStatus.CREATED);
    }

    // endpoint para obtener los gastos de una tarjeta de crédito específica
    @GetMapping("/{tarjetaCreditoID}/gastos")
    public ResponseEntity<List<GastoTarjeta>> getGastosByTarjeta(@PathVariable Long tarjetaCreditoID) {
        List<GastoTarjeta> gastos = gastoTarjetaRepository.findByTarjetaCredito_TarjetaCreditoID(tarjetaCreditoID);
        if (gastos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(gastos, HttpStatus.OK);
    }

    // endpoint para obtenerlo por fechas
    @GetMapping("/fecha")
    public ResponseEntity<List<TarjetaCredito>> listTarjetasByMonthAndYear(
            @PathVariable String usuarioID,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        if (year == null) {
            throw new IllegalArgumentException("El parámetro 'year' es requerido.");
        }

        if (month != null) {
            // Si se proporciona un mes, se obtienen las tarjetas de crédito solo para ese
            // mes y año
            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            List<TarjetaCredito> tarjetas = tarjetaCreditoRepository.findByUsuarioIDAndFechaBetween(usuarioID,
                    startDate, endDate);
            return new ResponseEntity<>(tarjetas, HttpStatus.OK);
        } else {
            // Si no se proporciona un mes, se obtienen las tarjetas de crédito para todo el
            // año
            LocalDate startDate = LocalDate.of(year, 1, 1);
            LocalDate endDate = startDate.withDayOfYear(startDate.lengthOfYear());
            List<TarjetaCredito> tarjetas = tarjetaCreditoRepository.findByUsuarioIDAndFechaBetween(usuarioID,
                    startDate, endDate);
            return new ResponseEntity<>(tarjetas, HttpStatus.OK);
        }
    }

    // endpoint para obtener los gastos de una tarjeta de crédito específica por
    // fecha
    @GetMapping("/{tarjetaCreditoID}/gastos/fecha")
    public ResponseEntity<List<GastoTarjeta>> getGastosByTarjetaAndMonthAndYear(
            @PathVariable String usuarioID,
            @PathVariable Long tarjetaCreditoID,
            @RequestParam int year,
            @RequestParam int month) {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<GastoTarjeta> gastos = gastoTarjetaRepository
                .findByTarjetaCredito_TarjetaCreditoIDAndTarjetaCredito_UsuarioIDAndFechaBetween(
                        tarjetaCreditoID,
                        usuarioID,
                        startDate,
                        endDate);

        if (gastos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(gastos, HttpStatus.OK);
    }

    // endpoint para borra un gasto de una tarjeta de crédito
    @DeleteMapping("/{tarjetaCreditoID}/gastos/{gastoID}")
    public ResponseEntity<Void> deleteGastoTarjeta(@PathVariable Long tarjetaCreditoID,
            @PathVariable Long gastoID) {
        GastoTarjeta gastoTarjeta = gastoTarjetaRepository.findById(gastoID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Gasto de tarjeta no encontrado con ID: " + gastoID));

        // Actualizar el valor total de la tarjeta de crédito
        TarjetaCredito tarjeta = gastoTarjeta.getTarjetaCredito();
        tarjeta.setValorTotal(tarjeta.getValorTotal().subtract(gastoTarjeta.getValorTotalGasto()));
        tarjetaCreditoRepository.save(tarjeta);

        gastoTarjetaRepository.delete(gastoTarjeta);
        return ResponseEntity.ok().build();
    }

    // endpoint para actualizar un gasto de una tarjeta de crédito
    @PatchMapping("/{tarjetaCreditoID}/gastos/{gastoID}")
    public ResponseEntity<GastoTarjeta> updateGastoTarjeta(@PathVariable Long tarjetaCreditoID,
            @PathVariable Long gastoID,
            @RequestBody GastoTarjeta updateRequest) {

        GastoTarjeta gastoTarjeta = gastoTarjetaRepository.findById(gastoID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Gasto de tarjeta no encontrado con ID: " + gastoID));

        // Restar el viejo valor del gasto del valor total de la tarjeta antes de
        // actualizar
        TarjetaCredito tarjeta = gastoTarjeta.getTarjetaCredito();
        tarjeta.setValorTotal(tarjeta.getValorTotal().subtract(gastoTarjeta.getValorTotalGasto()));

        boolean debeRecalcularCuota = false;

        if (updateRequest.getNombreGasto() != null) {
            gastoTarjeta.setNombreGasto(updateRequest.getNombreGasto());
        }

        if (updateRequest.getValorTotalGasto() != null &&
                !updateRequest.getValorTotalGasto().equals(gastoTarjeta.getValorTotalGasto())) {
            gastoTarjeta.setValorTotalGasto(updateRequest.getValorTotalGasto());
            debeRecalcularCuota = true;
        }

        if (updateRequest.getCuotaTotal() != null &&
                !updateRequest.getCuotaTotal().equals(gastoTarjeta.getCuotaTotal())) {
            gastoTarjeta.setCuotaTotal(updateRequest.getCuotaTotal());
            debeRecalcularCuota = true;
        }

        // Si hay cambios que requieran recalcular el valor de la cuota, hacerlo aquí
        if (debeRecalcularCuota) {
            BigDecimal valorCuota = gastoTarjeta.getValorTotalGasto()
                    .divide(new BigDecimal(gastoTarjeta.getCuotaTotal()), 2, RoundingMode.HALF_UP);
            gastoTarjeta.setValorCuotaGasto(valorCuota);
        }

        tarjeta.setValorTotal(tarjeta.getValorTotal().add(gastoTarjeta.getValorTotalGasto()));
        tarjetaCreditoRepository.save(tarjeta);

        GastoTarjeta updatedGastoTarjeta = gastoTarjetaRepository.save(gastoTarjeta);
        return ResponseEntity.ok(updatedGastoTarjeta);
    }

    // Este endpoint permite cambiar el tipo del gasto de una tarjeta de crédito.
    @PatchMapping("/{tarjetaCreditoID}/gastos/{gastoID}/tipo")
    public ResponseEntity<GastoTarjeta> updateGastoTarjetaTipo(
            @PathVariable Long tarjetaCreditoID,
            @PathVariable Long gastoID,
            @RequestBody TipoGastoRequest tipoGastoRequest) {

        GastoTarjeta gastoTarjeta = gastoTarjetaRepository.findById(gastoID)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Gasto Tarjeta no encontrado con ID: " + gastoID));

        TipoGasto nuevoTipo = TipoGasto.valueOf(tipoGastoRequest.getTipo());
        gastoTarjeta.setTipo(nuevoTipo);
        GastoTarjeta updatedGastoTarjeta = gastoTarjetaRepository.save(gastoTarjeta);

        return ResponseEntity.ok(updatedGastoTarjeta);
    }

}