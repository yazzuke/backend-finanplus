package co.finanplus.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import co.finanplus.api.domain.Gastos.Diario.GastoDiario;
import co.finanplus.api.domain.Gastos.Diario.GastoDiarioIndividual;
import co.finanplus.api.domain.Gastos.Diario.GastoDiarioIndividualRepository;
import co.finanplus.api.domain.Gastos.Diario.GastoDiarioRepository;
import co.finanplus.api.domain.Gastos.Diario.TipoDiario;
import co.finanplus.api.domain.Gastos.Diario.TipoGastoRequest;

@RestController
@RequestMapping("usuarios/{usuarioID}/gastosdiario")
public class GastoDiarioController {

    @Autowired
    private GastoDiarioRepository gastoDiarioRepository;

    @Autowired
    private GastoDiarioIndividualRepository gastoDiarioIndividualRepository;

    // endpoint para obtener los gastos diarios de un usuario
    @GetMapping
    public ResponseEntity<List<GastoDiario>> listGastosDiario(@PathVariable String usuarioID) {
        List<GastoDiario> gastosDiario = gastoDiarioRepository.findByUsuarioID(usuarioID);
        return new ResponseEntity<>(gastosDiario, HttpStatus.OK);
    }

    // endpoint para agregar los gastos diarios de un usuario
    @PostMapping
    public ResponseEntity<GastoDiario> addGastoDiario(@PathVariable String usuarioID,
            @RequestBody GastoDiario gastoDiario) {
        gastoDiario.setUsuarioID(usuarioID);
        gastoDiario.setFecha(LocalDate.now());
        GastoDiario savedGastoDiario = gastoDiarioRepository.save(gastoDiario);
        return new ResponseEntity<>(savedGastoDiario, HttpStatus.CREATED);
    }

    // endpoint para agregar los gastos individuales a un gasto diario específico
    @PostMapping("/{gastoDiarioID}/gastos")
    public ResponseEntity<GastoDiarioIndividual> addGastoIndividual(@PathVariable Long gastoDiarioID,
            @RequestBody GastoDiarioIndividual gasto) {
        GastoDiario gastoDiario = gastoDiarioRepository.findById(gastoDiarioID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Gasto Diario no encontrado con ID: " + gastoDiarioID));
        gasto.setGastoDiario(gastoDiario);
        gasto.setFecha(LocalDate.now());
        gasto.setFechaInsertado(LocalDate.now());

        BigDecimal totalValue = gastoDiario.getValorTotal();
        BigDecimal newTotalValue = totalValue.add(gasto.getValorTotalGasto());
        gastoDiario.setValorTotal(newTotalValue);

        GastoDiarioIndividual savedGasto = gastoDiarioIndividualRepository.save(gasto);
        gastoDiarioRepository.save(gastoDiario);

        return new ResponseEntity<>(savedGasto, HttpStatus.CREATED);
    }

    // Endpoint to retrieve individual expenses for a specific daily expense
    @GetMapping("/{gastoDiarioID}/gastos")
    public ResponseEntity<List<GastoDiarioIndividual>> getGastosByGastoDiario(@PathVariable Long gastoDiarioID) {
        List<GastoDiarioIndividual> gastos = gastoDiarioIndividualRepository
                .findByGastoDiario_GastoDiarioID(gastoDiarioID);
        if (gastos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(gastos, HttpStatus.OK);
    }

    // Endpoint to obtain general daily expenses by month and year
    @GetMapping("/fecha")
    public ResponseEntity<List<GastoDiario>> listGastosDiarioByMonthAndYear(
            @PathVariable String usuarioID,
            @RequestParam int year,
            @RequestParam int month) {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<GastoDiario> gastosFijos = gastoDiarioRepository.findByUsuarioIDAndFechaBetween(usuarioID, startDate,
                endDate);
        return new ResponseEntity<>(gastosFijos, HttpStatus.OK);
    }

    // Endpoint to obtain individual expenses of a specific daily expense by month
    // and year
    @GetMapping("/{gastoDiarioID}/gastos/fecha")
    public ResponseEntity<List<GastoDiarioIndividual>> getGastosIndividualesByGastoDiarioAndMonthAndYear(
            @PathVariable Long gastoDiarioID,
            @PathVariable String usuarioID,
            @RequestParam int year,
            @RequestParam int month) {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<GastoDiarioIndividual> gastos = gastoDiarioIndividualRepository
                .findByGastoDiario_GastoDiarioIDAndGastoDiario_UsuarioIDAndFechaInsertadoBetween(
                        gastoDiarioID,
                        usuarioID,
                        startDate,
                        endDate);
        if (gastos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(gastos, HttpStatus.OK);
    }

    // Endpoint to update the type of an individual daily expense
    @PatchMapping("/{gastoDiarioID}/gastos/{gastoID}/tipo")
    public ResponseEntity<GastoDiarioIndividual> updateGastoDiarioTipo(
            @PathVariable Long gastoDiarioID,
            @PathVariable Long gastoID,
            @RequestBody TipoGastoRequest tipoGastoRequest) {

        GastoDiarioIndividual gastoDiarioIndividual = gastoDiarioIndividualRepository.findById(gastoID)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Gasto Diario Individual no encontrado con ID: " + gastoID));

        TipoDiario nuevoTipo = TipoDiario.valueOf(tipoGastoRequest.getTipo());
        gastoDiarioIndividual.setTipo(nuevoTipo);
        GastoDiarioIndividual updatedGastoDiarioIndividual = gastoDiarioIndividualRepository
                .save(gastoDiarioIndividual);

        return ResponseEntity.ok(updatedGastoDiarioIndividual);
    }

    @DeleteMapping("/{gastoDiarioID}/gastos/{gastoID}")
    public ResponseEntity<Void> deleteGastoIndividual(@PathVariable Long gastoDiarioID, @PathVariable Long gastoID) {
        GastoDiarioIndividual gasto = gastoDiarioIndividualRepository.findById(gastoID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Gasto individual no encontrado con ID: " + gastoID));

        gastoDiarioIndividualRepository.delete(gasto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{gastoDiarioID}/gastos/{gastoID}")
    public ResponseEntity<GastoDiarioIndividual> updateGastoIndividual(
            @PathVariable Long gastoDiarioID,
            @PathVariable Long gastoID,
            @RequestBody GastoDiarioIndividual updateRequest) {

        GastoDiarioIndividual gasto = gastoDiarioIndividualRepository.findById(gastoID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Gasto individual no encontrado con ID: " + gastoID));

        if (updateRequest.getNombreGasto() != null) {
            gasto.setNombreGasto(updateRequest.getNombreGasto());
        }
        if (updateRequest.getValorGasto() != null) {
            gasto.setValorGasto(updateRequest.getValorGasto());
        }
        if (updateRequest.getFecha() != null) {
            gasto.setFecha(updateRequest.getFecha());
        }

        GastoDiarioIndividual updatedGasto = gastoDiarioIndividualRepository.save(gasto);
        return ResponseEntity.ok(updatedGasto);
    }

}
