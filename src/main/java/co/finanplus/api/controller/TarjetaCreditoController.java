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
import java.math.BigDecimal;
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
        gasto.setFecha(LocalDate.now());

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

    // Más métodos para actualizar y eliminar tarjetas y gastos si es necesario
}
