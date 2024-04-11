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
import co.finanplus.api.domain.Gastos.Tarjetas.GastoTarjeta;
import co.finanplus.api.domain.Gastos.Tarjetas.GastoTarjetaRepository;
import co.finanplus.api.domain.Gastos.Tarjetas.TarjetaCredito;
import co.finanplus.api.domain.Gastos.Tarjetas.TarjetaCreditoRepository;

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
        GastoInvFijo savedGasto = gastoInvFijoRepository.save(gasto);
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

}
