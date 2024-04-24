package co.finanplus.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.finanplus.api.domain.Ingresos.Ingreso;
import co.finanplus.api.domain.Ingresos.IngresoRepository;
import co.finanplus.api.service.IngresoService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/usuarios/{usuarioID}/ingresos")
public class IngresoController {

    @Autowired
    private IngresoRepository ingresoRepository;

    
    private final IngresoService ingresoService;

    @Autowired
    public IngresoController(IngresoService ingresoService) {
        this.ingresoService = ingresoService;
    }

  

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
        ingreso.setFecha(LocalDate.now());
        Ingreso savedIngreso = ingresoService.agregarIngreso(ingreso);
        return new ResponseEntity<>(savedIngreso, HttpStatus.CREATED);
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<Ingreso>> listIngresosByMonthAndYear(
            @PathVariable String usuarioID,
            @RequestParam int year,
            @RequestParam int month) {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Ingreso> ingresos = ingresoRepository.findByUsuarioIDAndFechaBetween(usuarioID, startDate, endDate);
        return new ResponseEntity<>(ingresos, HttpStatus.OK);
    }
   // Endpoint para actualizar un ingreso específico
   @PutMapping("/{ingresoID}")
   public ResponseEntity<Ingreso> updateIngreso(
           @PathVariable String usuarioID,
           @PathVariable Long ingresoID,
           @RequestBody Ingreso ingresoDetails) {
       Ingreso updatedIngreso = ingresoService.actualizarIngreso(ingresoID, ingresoDetails);
       if (updatedIngreso != null) {
           return new ResponseEntity<>(updatedIngreso, HttpStatus.OK);
       } else {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
   }

   // Endpoint para eliminar un ingreso específico
   @DeleteMapping("/{ingresoID}")
   public ResponseEntity<HttpStatus> deleteIngreso(
           @PathVariable String usuarioID,
           @PathVariable Long ingresoID) {
       boolean isDeleted = ingresoService.eliminarIngreso(ingresoID);
       if (isDeleted) {
           return new ResponseEntity<>(HttpStatus.NO_CONTENT);
       } else {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
   }
}