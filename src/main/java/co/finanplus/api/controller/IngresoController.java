package co.finanplus.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import co.finanplus.api.domain.Ingresos.Ingreso;
import co.finanplus.api.domain.Ingresos.IngresoRepository;

import java.util.List;

@RestController
@RequestMapping("/usuarios/{usuarioID}/ingresos")
public class IngresoController {

    @Autowired
    private IngresoRepository ingresoRepository;

    @GetMapping
    public List<Ingreso> listIngresos(@PathVariable String usuarioID) {
        return ingresoRepository.findByUsuarioID(usuarioID);
    }

    @PostMapping
    public Ingreso addIngreso(@PathVariable String usuarioID, @RequestBody Ingreso ingreso) {
        ingreso.setUsuarioID(usuarioID);
        return ingresoRepository.save(ingreso);
    }

    // Más métodos para actualizar y eliminar ingresos
}