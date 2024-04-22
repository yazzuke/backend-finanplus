package co.finanplus.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import co.finanplus.api.domain.Gastos.Fijos.GastoFijo;
import co.finanplus.api.domain.Gastos.Fijos.GastoFijoRepository;
import co.finanplus.api.domain.Gastos.Fijos.GastoInvFijo;
import co.finanplus.api.domain.Gastos.Fijos.GastoInvFijoRepository;
import co.finanplus.api.domain.Gastos.Fijos.TipoFijo;
import co.finanplus.api.domain.Gastos.Fijos.TipoGastoRequest;
import co.finanplus.api.domain.Gastos.Tarjetas.GastoTarjeta;
import co.finanplus.api.domain.Gastos.Tarjetas.GastoTarjetaRepository;
import co.finanplus.api.domain.Gastos.Tarjetas.TarjetaCredito;
import co.finanplus.api.domain.Gastos.Tarjetas.TarjetaCreditoRepository;
import co.finanplus.api.domain.Gastos.Tarjetas.TipoGasto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/usuarios/{usuarioID}/gastosfijos")
public class GastoFijoController {

    @Autowired
    private GastoFijoRepository gastoFijoRepository;

    @Autowired
    private GastoInvFijoRepository gastoInvFijoRepository;

    // endpoint para obtener los gastos fijos de un usuario
    @GetMapping
    public ResponseEntity<List<GastoFijo>> listGastosFijos(@PathVariable String usuarioID) {
        List<GastoFijo> gastosFijos = gastoFijoRepository.findByUsuarioID(usuarioID);
        return new ResponseEntity<>(gastosFijos, HttpStatus.OK);
    }

    // endpoint para agregar un gasto fijo a un usuario
    @PostMapping
    public ResponseEntity<GastoFijo> addGastoFijo(@PathVariable String usuarioID,
            @RequestBody GastoFijo gastoFijo) {
        gastoFijo.setUsuarioID(usuarioID);
        gastoFijo.setFecha(LocalDate.now());
        GastoFijo savedGastoFijo = gastoFijoRepository.save(gastoFijo);
        return new ResponseEntity<>(savedGastoFijo, HttpStatus.CREATED);
    }

    // endpoint para agregar los gastos a un gastofijo específico
    @PostMapping("/{gastoFijoID}/gastos")
    public ResponseEntity<GastoInvFijo> addGastoAFijo(@PathVariable Long gastoFijoID,
            @RequestBody GastoInvFijo gasto) {
        GastoFijo gastoFijo = gastoFijoRepository.findById(gastoFijoID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Gasto Fijo no encontrado con ID: " + gastoFijoID));
        gasto.setGastoFijo(gastoFijo);
        gasto.setFechaInsertado(LocalDate.now());

        // Agrega el valor del gasto al total actual del gasto fijo
        BigDecimal valorTotalActual = gastoFijo.getValorTotal();
        if (valorTotalActual == null) {
            valorTotalActual = BigDecimal.ZERO;
        }
        BigDecimal nuevoValorTotal = valorTotalActual.add(gasto.getValorTotalGasto());
        gastoFijo.setValorTotal(nuevoValorTotal);

        // Guarda el gasto y actualiza el gasto fijo
        GastoInvFijo savedGasto = gastoInvFijoRepository.save(gasto);
        gastoFijoRepository.save(gastoFijo); // Guarda el gasto fijo con el nuevo valor total

        return new ResponseEntity<>(savedGasto, HttpStatus.CREATED);
    }

    // endpoint para obtener los gastos de un gasto fijo específico
    @GetMapping("/{gastoFijoID}/gastos")
    public ResponseEntity<List<GastoInvFijo>> getGastosByGastoFijo(@PathVariable Long gastoFijoID) {
        List<GastoInvFijo> gastos = gastoInvFijoRepository.findByGastoFijo_GastoFijoID(gastoFijoID);
        if (gastos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(gastos, HttpStatus.OK);
    }

    // endpoint para obtener los gastos generales por fecha
    @GetMapping("/fecha")
    public ResponseEntity<List<GastoFijo>> listGastosFijosByMonthAndYear(
            @PathVariable String usuarioID,
            @RequestParam int year,
            @RequestParam int month) {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<GastoFijo> gastosFijos = gastoFijoRepository.findByUsuarioIDAndFechaBetween(usuarioID, startDate, endDate);
        return new ResponseEntity<>(gastosFijos, HttpStatus.OK);
    }

    // endpoint para obtener los gastos de un gasto fijo específico por fecha
    @GetMapping("/{gastoFijoID}/gastos/fecha")
    public ResponseEntity<List<GastoInvFijo>> getGastosByGastoFijoAndMonthAndYear(
            @PathVariable String usuarioID,
            @PathVariable Long gastoFijoID,
            @RequestParam int year,
            @RequestParam int month) {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<GastoInvFijo> gastos = gastoInvFijoRepository
                .findByGastoFijo_GastoFijoIDAndGastoFijo_UsuarioIDAndFechaInsertadoBetween(
                        gastoFijoID,
                        usuarioID,
                        startDate,
                        endDate);

        if (gastos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(gastos, HttpStatus.OK);
    }

    // endpoint para actualizar el tipo de un gasto fijo
    @PatchMapping("/{gastoFijoID}/gastos/{gastoID}/tipo")
    public ResponseEntity<GastoInvFijo> updateGastoFijoTipo(
            @PathVariable Long gastoFijoID,
            @PathVariable Long gastoID,
            @RequestBody TipoGastoRequest tipoGastoRequest) {

        GastoInvFijo gastoInvFijo = gastoInvFijoRepository.findById(gastoID)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Gasto Fijo no encontrado con ID: " + gastoID));

        TipoFijo nuevoTipo = TipoFijo.valueOf(tipoGastoRequest.getTipo());
        gastoInvFijo.setTipo(nuevoTipo);
        GastoInvFijo updatedGastoFijo = gastoInvFijoRepository.save(gastoInvFijo);

        return ResponseEntity.ok(updatedGastoFijo);
    }

    // endpoint para eliminar un gasto fijo
    @DeleteMapping("/{gastoFijoID}/gastos/{gastoInvFijoID}")
    public ResponseEntity<Void> deleteGastoFijoIndividual(@PathVariable Long gastoFijoID,
            @PathVariable Long gastoInvFijoID) {
        GastoInvFijo gasto = gastoInvFijoRepository.findById(gastoInvFijoID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Gasto fijo individual no encontrado con ID: " + gastoInvFijoID));

        gastoInvFijoRepository.delete(gasto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{gastoFijoID}/gastos/{gastoInvFijoID}")
    public ResponseEntity<GastoInvFijo> updateGastoFijoIndividual(
            @PathVariable Long gastoFijoID,
            @PathVariable Long gastoInvFijoID,
            @RequestBody GastoInvFijo updateRequest) {
    
        GastoInvFijo gasto = gastoInvFijoRepository.findById(gastoInvFijoID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Gasto fijo individual no encontrado con ID: " + gastoInvFijoID));
    
        if (updateRequest.getNombreGasto() != null) {
            gasto.setNombreGasto(updateRequest.getNombreGasto());
        }
        if (updateRequest.getValorGasto() != null) {
            gasto.setValorGasto(updateRequest.getValorGasto());
        }
        if (updateRequest.getFechaInsertado() != null) {
            gasto.setFechaInsertado(updateRequest.getFechaInsertado());
        }
    
        GastoInvFijo updatedGasto = gastoInvFijoRepository.save(gasto);
        return ResponseEntity.ok(updatedGasto);
    }
    

}
