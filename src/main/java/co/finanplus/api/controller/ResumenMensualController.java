package co.finanplus.api.controller;

import co.finanplus.api.domain.ResumenMensual.ResumenMensual;
import co.finanplus.api.domain.ResumenMensual.ResumenMensualRepository;
import co.finanplus.api.dto.GastosUpdateRequest;
import co.finanplus.api.dto.TotalesMensualesDTO;
import co.finanplus.api.service.ResumenMensualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PatchMapping;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios/{usuarioID}/resumenmensual")
public class ResumenMensualController {

    @Autowired
    private ResumenMensualService resumenMensualService;

    @Autowired
    private ResumenMensualRepository resumenMensualRepository;

    @GetMapping
    public ResponseEntity<List<ResumenMensual>> getResumenMensualByUsuarioID(@PathVariable String usuarioID) {
        List<ResumenMensual> resumenes = resumenMensualService.findByUsuarioID(usuarioID);
        if (resumenes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resumenes);

    }
    
    @PatchMapping("/{resumenID}/updateGastos")
    public ResponseEntity<ResumenMensual> patchTotalGastos(@PathVariable Long resumenID,
            @RequestBody GastosUpdateRequest request) {
        ResumenMensual resumen = resumenMensualRepository.findById(resumenID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resumen no encontrado"));
        resumen.setTotalGastos(request.getTotalGastos());
        
        // Recalcula el balance
        BigDecimal balance = resumen.getTotalIngresos().subtract(request.getTotalGastos());
        resumen.setBalance(balance);
        
        resumenMensualRepository.save(resumen);
        return ResponseEntity.ok(resumen);
    }
    

    // trae un solo resumen por año y mes
    @GetMapping("/fecha")
    public ResponseEntity<List<ResumenMensual>> getResumenMensualByMonthAndYear(
            @PathVariable String usuarioID,
            @RequestParam int year,
            @RequestParam int month) {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<ResumenMensual> resumenes = resumenMensualRepository.findByUsuarioIDAndFechaInicioBetween(usuarioID,
                startDate, endDate);
        if (resumenes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resumenes);
    }

 


    // trae TODOS los resumenes por año y por mes
    @GetMapping("/totales")
public ResponseEntity<List<TotalesMensualesDTO>> getTotalesByUsuario(
        @PathVariable String usuarioID,
        @RequestParam(required = false) Integer year) {

    List<TotalesMensualesDTO> totales = resumenMensualRepository.findTotalesByUsuarioIDAndYear(usuarioID, year);
    if (totales.isEmpty()) {
        return ResponseEntity.notFound().build();
    } else {
        return ResponseEntity.ok(totales);
    }
}


}
