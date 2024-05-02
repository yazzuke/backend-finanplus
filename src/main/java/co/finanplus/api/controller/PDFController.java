package co.finanplus.api.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import co.finanplus.api.service.PDFGeneratorService;

@RestController
public class PDFController {

    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    @GetMapping("/usuarios/{userId}/generarPDF")
    public ResponseEntity<byte[]> generarPDF(@PathVariable String userId) {
        byte[] pdfBytes = pdfGeneratorService.generarPDF(userId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "informe.pdf");
        headers.setContentLength(pdfBytes.length);
        
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
    
}
