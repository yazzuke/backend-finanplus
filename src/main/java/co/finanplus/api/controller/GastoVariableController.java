package co.finanplus.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
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
import java.util.Map;

import co.finanplus.api.domain.Gastos.Diario.GastoDiario;
import co.finanplus.api.domain.Gastos.Diario.GastoDiarioIndividual;
import co.finanplus.api.domain.Gastos.Diario.GastoDiarioIndividualRepository;
import co.finanplus.api.domain.Gastos.Diario.GastoDiarioRepository;
import co.finanplus.api.domain.Gastos.Diario.TipoDiario;
import co.finanplus.api.domain.Gastos.Diario.TipoGastoRequest;
import co.finanplus.api.domain.Gastos.Variables.GastoVariable;
import co.finanplus.api.domain.Gastos.Variables.GastoVariableIndividual;
import co.finanplus.api.domain.Gastos.Variables.GastoVariableIndividualRepository;
import co.finanplus.api.domain.Gastos.Variables.GastoVariableRepository;
import co.finanplus.api.domain.Gastos.Variables.TipoVariable;

@RestController
@RequestMapping("usuarios/{usuarioID}/gastosvariables")
public class GastoVariableController {

    @Autowired
    private GastoVariableRepository gastoVariableRepository;

    @Autowired
    private GastoVariableIndividualRepository gastoVariableIndividualRepository;

    // endpoint para obtener los gastos variables de un usuario
    @GetMapping
    public ResponseEntity<List<GastoVariable>> ListGastosVariables(@PathVariable String usuarioID) {
        List<GastoVariable> gastosVariables = gastoVariableRepository.findByUsuarioID(usuarioID);
        return ResponseEntity.ok(gastosVariables);
    }

    // Agregar un gasto variable a un usuario
    @PostMapping
    public ResponseEntity<GastoVariable> addGastoVariable(@PathVariable String usuarioID,
            @RequestBody GastoVariable gastoVariable) {
        gastoVariable.setUsuarioID(usuarioID);
        gastoVariable.setFecha(LocalDate.now());
        GastoVariable savedGastoVariable = gastoVariableRepository.save(gastoVariable);
        return new ResponseEntity<>(savedGastoVariable, HttpStatus.CREATED);
    }

    // Agregar un gasto variable individual a un gasto variable específico
    @PostMapping("/{gastoVariableID}/gastos")
    public ResponseEntity<GastoVariableIndividual> addGastoVariableIndividual(
            @PathVariable Long gastoVariableID, @RequestBody GastoVariableIndividual gasto) {
        GastoVariable gastoVariable = gastoVariableRepository.findById(gastoVariableID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Gasto Variable no encontrado con ID: " + gastoVariableID));
        gasto.setGastoVariable(gastoVariable);
        gasto.setFechaInsertado(LocalDate.now());

        BigDecimal totalValue = gastoVariable.getValorTotal();
        BigDecimal newTotalValue = totalValue.add(gasto.getValorTotalGasto());
        gastoVariable.setValorTotal(newTotalValue);

        GastoVariableIndividual savedGasto = gastoVariableIndividualRepository.save(gasto);
        gastoVariableRepository.save(gastoVariable);

        return new ResponseEntity<>(savedGasto, HttpStatus.CREATED);
    }

    // Obtener los gastos variables individuales para un gasto variable específico
    @GetMapping("/{gastoVariableID}/gastos")
    public ResponseEntity<List<GastoVariableIndividual>> getGastosByGastoVariable(@PathVariable Long gastoVariableID) {
        List<GastoVariableIndividual> gastos = gastoVariableIndividualRepository
                .findByGastoVariable_GastoVariableID(gastoVariableID);
        if (gastos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(gastos, HttpStatus.OK);
    }

    // Endpoint para obtener gastos variables generales por mes y año
    @GetMapping("/fecha")
    public ResponseEntity<List<GastoVariable>> listGastosVariablesByMonthAndYear(
            @PathVariable String usuarioID,
            @RequestParam int year,
            @RequestParam int month) {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<GastoVariable> gastosVariables = gastoVariableRepository.findByUsuarioIDAndFechaBetween(
                usuarioID, startDate, endDate);
        return new ResponseEntity<>(gastosVariables, HttpStatus.OK);
    }

    // Endpoint para obtener gastos variables individuales de un gasto variable
    // específico por mes y año
    @GetMapping("/{gastoVariableID}/gastos/fecha")
    public ResponseEntity<List<GastoVariableIndividual>> getGastosIndividualesByGastoVariableAndMonthAndYear(
            @PathVariable Long gastoVariableID,
            @PathVariable String usuarioID,
            @RequestParam int year,
            @RequestParam int month) {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<GastoVariableIndividual> gastos = gastoVariableIndividualRepository
                .findByGastoVariable_GastoVariableIDAndGastoVariable_UsuarioIDAndFechaInsertadoBetween(
                        gastoVariableID, usuarioID, startDate, endDate);
        if (gastos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(gastos, HttpStatus.OK);
    }

    // Endpoint para actualizar el tipo de un gasto variable individual
    @PatchMapping("/{gastoVariableID}/gastos/{gastoID}/tipo")
    public ResponseEntity<GastoVariableIndividual> updateGastoVariableTipo(
            @PathVariable Long gastoVariableID,
            @PathVariable Long gastoID,
            @RequestBody TipoGastoRequest tipoGastoRequest) {

        GastoVariableIndividual gastoVariableIndividual = gastoVariableIndividualRepository.findById(gastoID)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Gasto Variable Individual no encontrado con ID: " + gastoID));

        TipoVariable nuevoTipo = TipoVariable.valueOf(tipoGastoRequest.getTipo());
        gastoVariableIndividual.setTipo(nuevoTipo);
        GastoVariableIndividual updatedGastoVariableIndividual = gastoVariableIndividualRepository
                .save(gastoVariableIndividual);

        return ResponseEntity.ok(updatedGastoVariableIndividual);
    }

    // Endpoint para eliminar un gasto variable individual
    @DeleteMapping("/{gastoVariableID}/gastos/{gastoIndividualID}")
    public ResponseEntity<Void> deleteGastoVariableIndividual(@PathVariable Long gastoVariableID,
                                                              @PathVariable Long gastoIndividualID) {
        GastoVariableIndividual gastoIndividual = gastoVariableIndividualRepository.findById(gastoIndividualID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Gasto Variable Individual no encontrado con ID: " + gastoIndividualID));
    
        GastoVariable gastoVariable = gastoIndividual.getGastoVariable();
        gastoVariable.setValorTotal(gastoVariable.getValorTotal().subtract(gastoIndividual.getValorTotalGasto()));
        gastoVariableRepository.save(gastoVariable); // Guarda el gasto variable con el nuevo valor total
    
        gastoVariableIndividualRepository.delete(gastoIndividual);
        return ResponseEntity.ok().build();
    }
    

    @PatchMapping("/{gastoVariableID}/gastos/{gastoIndividualID}")
    public ResponseEntity<GastoVariableIndividual> updateGastoVariableIndividual(
            @PathVariable Long gastoVariableID,
            @PathVariable Long gastoIndividualID,
            @RequestBody GastoVariableIndividual updateRequest) {
    
        GastoVariableIndividual gastoIndividual = gastoVariableIndividualRepository.findById(gastoIndividualID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Gasto Variable Individual no encontrado con ID: " + gastoIndividualID));
    
        GastoVariable gastoVariable = gastoIndividual.getGastoVariable();
        BigDecimal valorAntiguo = gastoIndividual.getValorTotalGasto();
    
        // Actualizar campos necesarios
        if (updateRequest.getValorGasto() != null) {
            gastoIndividual.setValorGasto(updateRequest.getValorGasto());
            // Si se actualiza el valor del gasto, recalcular el valor total del gasto variable
            BigDecimal valorTotalActual = gastoVariable.getValorTotal();
            BigDecimal valorNuevo = updateRequest.getValorGasto();
            BigDecimal valorTotalNuevo = valorTotalActual.subtract(valorAntiguo).add(valorNuevo);
            gastoVariable.setValorTotal(valorTotalNuevo);
            gastoVariableRepository.save(gastoVariable);
        }
        // Otras actualizaciones si son necesarias...
    
        GastoVariableIndividual updatedGastoIndividual = gastoVariableIndividualRepository.save(gastoIndividual);
        return ResponseEntity.ok(updatedGastoIndividual);
    }
    

    // Endpoint para actualizar el estado de pago de un gasto variable individual
    @PatchMapping("/{gastoVariableID}/gastos/{gastoIndividualID}/pagado")
    public ResponseEntity<GastoVariableIndividual> updateGastoVariablePagado(
            @PathVariable Long gastoVariableID,
            @PathVariable Long gastoIndividualID,
            @RequestBody Map<String, Boolean> pagadoStatus) {

        GastoVariableIndividual gastoIndividual = gastoVariableIndividualRepository.findById(gastoIndividualID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Gasto Variable Individual no encontrado con ID: " + gastoIndividualID));

        Boolean isPagado = pagadoStatus.get("pagado");
        if (isPagado == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado de 'pagado' no proporcionado");
        }
        gastoIndividual.setPagado(isPagado);
        GastoVariableIndividual updatedGastoIndividual = gastoVariableIndividualRepository.save(gastoIndividual);
        return ResponseEntity.ok(updatedGastoIndividual);
    }

}
