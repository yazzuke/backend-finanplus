package co.finanplus.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.finanplus.api.domain.Ahorros.Ahorro;
import co.finanplus.api.domain.Ahorros.AhorroRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/usuarios/{usuarioID}/ahorros")
public class AhorroController {

    @Autowired
    private AhorroRepository ahorrosRepository;

    // endpoint para obtener los ahorros de un usuario
    @GetMapping
    public ResponseEntity<List<Ahorro>> listAhorros(@PathVariable String usuarioID) {
        List<Ahorro> ahorros = ahorrosRepository.findByUsuarioID(usuarioID);
        return new ResponseEntity<>(ahorros, HttpStatus.OK);
    }

    // endpoint para agregar un ahorro a un usuario
    @PostMapping
    public ResponseEntity<Ahorro> addAhorro(@PathVariable String usuarioID, @RequestBody Ahorro ahorro) {
        ahorro.setUsuarioID(usuarioID);
        ahorro.setFecha(LocalDate.now());
        Ahorro savedAhorro = ahorrosRepository.save(ahorro);
        return new ResponseEntity<>(savedAhorro, HttpStatus.CREATED);
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<Ahorro>> listAhorrosByMonthAndYear(
            @PathVariable String usuarioID,
            @RequestParam int year,
            @RequestParam int month) {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Ahorro> ahorros = ahorrosRepository.findByUsuarioIDAndFechaBetween(usuarioID, startDate, endDate);
        return new ResponseEntity<>(ahorros, HttpStatus.OK);
    }
    // Endpoint para actualizar un ahorro existente
            @PutMapping("/{ahorroID}")
            public ResponseEntity<Ahorro> updateAhorro(@PathVariable String usuarioID, @PathVariable Long ahorroID, @RequestBody Ahorro ahorroDetails) {
                Ahorro ahorro = ahorrosRepository.findById(ahorroID)
                    .orElseThrow(() -> new RuntimeException("Ahorro no encontrado con ID: " + ahorroID));

                // Actualiza solo los campos que pueden cambiar
                ahorro.setConcepto(ahorroDetails.getConcepto());
                ahorro.setMeta(ahorroDetails.getMeta());
                ahorro.setActual(ahorroDetails.getActual());
                ahorro.setTipo(ahorroDetails.getTipo());
                
                Ahorro updatedAhorro = ahorrosRepository.save(ahorro);
                return new ResponseEntity<>(updatedAhorro, HttpStatus.OK);
            }

                        // Endpoint para eliminar un ahorro
            @DeleteMapping("/{ahorroID}")
            public ResponseEntity<?> deleteAhorro(@PathVariable String usuarioID, @PathVariable Long ahorroID) {
                Ahorro ahorro = ahorrosRepository.findById(ahorroID)
                    .orElseThrow(() -> new RuntimeException("Ahorro no encontrado con ID: " + ahorroID));

                ahorrosRepository.delete(ahorro);
                return ResponseEntity.noContent().build();
            }

    



    // Más métodos para actualizar y eliminar ahorros
}