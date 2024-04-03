package co.finanplus.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.finanplus.api.domain.Ingresos.Ingreso;
import co.finanplus.api.domain.Ingresos.IngresoRepository;

import java.util.List;

@RestController
@RequestMapping("/usuarios/{usuarioID}/ingresos")
public class IngresoController {

    @Autowired
    private IngresoRepository ingresoRepository;

    // endpoint para obtener los ingresos de un usuario
    @GetMapping
    public ResponseEntity<List<Ingreso>> listIngresos(@PathVariable String usuarioID) {
        List<Ingreso> ingresos = ingresoRepository.findByUsuarioID(usuarioID);
        return new ResponseEntity<>(ingresos, HttpStatus.OK);
    }

    // endpoint para agregar un ingreso a un usuario
    @PostMapping
    public ResponseEntity<Ingreso> addIngreso(@PathVariable String usuarioID, @RequestBody Ingreso ingreso) {
        ingreso.setUsuarioID(usuarioID);
        Ingreso savedIngreso = ingresoRepository.save(ingreso);
        return new ResponseEntity<>(savedIngreso, HttpStatus.CREATED);
    }

    // Más métodos para actualizar y eliminar ingresos
}