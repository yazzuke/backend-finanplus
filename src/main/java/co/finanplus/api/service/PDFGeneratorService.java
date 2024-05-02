package co.finanplus.api.service;

import java.net.MalformedURLException;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import co.finanplus.api.domain.Gastos.Tarjetas.GastoTarjeta;
import co.finanplus.api.domain.Gastos.Tarjetas.GastoTarjetaRepository;
import co.finanplus.api.domain.Gastos.Tarjetas.TarjetaCredito;
import co.finanplus.api.domain.Gastos.Tarjetas.TarjetaCreditoRepository;




@Service
public class PDFGeneratorService {

    @Autowired
    private GastoTarjetaRepository gastoTarjetaRepository;

    @Autowired
    private TarjetaCreditoRepository tarjetaCreditoRepository;

    public byte[] generarPDF(String userId) {
        List<TarjetaCredito> tarjetasCredito = tarjetaCreditoRepository.findByUsuarioID(userId);
        if (tarjetasCredito.isEmpty()) {
            // Manejo de error: usuario sin tarjetas de crédito
            return null;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, outputStream);
            doc.open();

            for (TarjetaCredito tarjetaCredito : tarjetasCredito) {
                doc.add(new Paragraph("Tarjeta de Crédito: " + tarjetaCredito.getNombreTarjeta()));
                doc.add(new Paragraph("Fecha de Pago: " + tarjetaCredito.getFechaPago().toString()));
                doc.add(new Paragraph("Valor Total: " + tarjetaCredito.getValorTotal().toString()));
                doc.add(new Paragraph("\n"));

                List<GastoTarjeta> gastos = tarjetaCredito.getGastos();
                doc.add(new Paragraph("Gastos:"));
                for (GastoTarjeta gasto : gastos) {
                    doc.add(new Paragraph("Nombre: " + gasto.getNombreGasto()));
                    doc.add(new Paragraph("Cuota Total: " + gasto.getCuotaTotal()));
                    doc.add(new Paragraph("Cuota Actual: " + gasto.getCuotaActual()));
                    doc.add(new Paragraph("Valor Cuota Gasto: " + gasto.getValorCuotaGasto().toString()));
                    doc.add(new Paragraph("Valor Total Gasto: " + gasto.getValorTotalGasto().toString()));
                    doc.add(new Paragraph("Tipo: " + gasto.getTipo().toString()));
                    doc.add(new Paragraph("Fecha: " + gasto.getFecha().toString()));
                    doc.add(new Paragraph("\n"));
                }
                doc.add(new Paragraph("\n"));
            }

        } catch (DocumentException e) {
            // Manejo de error al generar el PDF
            e.printStackTrace();
        } finally {
            if (doc != null) {
                doc.close();
            }
        }

        return outputStream.toByteArray();
    }
}
