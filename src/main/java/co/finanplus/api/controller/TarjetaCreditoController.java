package co.finanplus.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.finanplus.api.domain.Gastos.TarjetaCredito;
import co.finanplus.api.domain.Gastos.TarjetaCreditoRepository;

import java.util.List;

@RestController
@RequestMapping("/usuarios/{usuarioID}/tarjetascredito")
public class TarjetaCreditoController {

    @Autowired
    private TarjetaCreditoRepository tarjetaCreditoRepository;

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
        TarjetaCredito savedTarjeta = tarjetaCreditoRepository.save(tarjeta);
        return new ResponseEntity<>(savedTarjeta, HttpStatus.CREATED);
    }

    // Más métodos para actualizar y eliminar tarjetas
}
