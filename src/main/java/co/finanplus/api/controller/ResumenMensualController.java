package co.finanplus.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.finanplus.api.domain.ResumenMensual.ResumenMensual;
import co.finanplus.api.service.ResumenMensualService;

@RestController
@RequestMapping("/usuarios/resumenmensual")
public class ResumenMensualController {

    private final ResumenMensualService resumenMensualService;

    public ResumenMensualController(ResumenMensualService resumenMensualService) {
        this.resumenMensualService = resumenMensualService;
    }



}