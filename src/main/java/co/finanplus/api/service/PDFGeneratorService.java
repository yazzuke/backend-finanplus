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

import co.finanplus.api.domain.Gastos.Diario.GastoDiario;
import co.finanplus.api.domain.Gastos.Diario.GastoDiarioIndividual;
import co.finanplus.api.domain.Gastos.Diario.GastoDiarioRepository;
import co.finanplus.api.domain.Gastos.Fijos.GastoFijo;
import co.finanplus.api.domain.Gastos.Fijos.GastoFijoRepository;
import co.finanplus.api.domain.Gastos.Fijos.GastoInvFijo;
import co.finanplus.api.domain.Gastos.Tarjetas.GastoTarjeta;
import co.finanplus.api.domain.Gastos.Tarjetas.GastoTarjetaRepository;
import co.finanplus.api.domain.Gastos.Tarjetas.TarjetaCredito;
import co.finanplus.api.domain.Gastos.Tarjetas.TarjetaCreditoRepository;
import co.finanplus.api.domain.Gastos.Variables.GastoVariable;
import co.finanplus.api.domain.Gastos.Variables.GastoVariableIndividual;
import co.finanplus.api.domain.Gastos.Variables.GastoVariableRepository;

@Service
public class PDFGeneratorService {

    @Autowired
    private GastoTarjetaRepository gastoTarjetaRepository;

    @Autowired
    private TarjetaCreditoRepository tarjetaCreditoRepository;

    @Autowired
    private GastoFijoRepository gastoFijoRepository;

    @Autowired
    private GastoVariableRepository gastoVariableRepository;

        
    @Autowired
    private GastoDiarioRepository gastoDiarioRepository;

    public byte[] generarPDF(String userId) {
        List<TarjetaCredito> tarjetasCredito = tarjetaCreditoRepository.findByUsuarioID(userId);
        List<GastoFijo> gastosFijos = gastoFijoRepository.findByUsuarioID(userId);
        List<GastoVariable> gastosVariables = gastoVariableRepository.findByUsuarioID(userId);
         List<GastoDiario> gastosDiarios = gastoDiarioRepository.findByUsuarioID(userId);



         if (tarjetasCredito.isEmpty() && gastosFijos.isEmpty() && gastosDiarios.isEmpty() && gastosVariables.isEmpty())  {
            // Manejo de error: usuario sin tarjetas de crédito, gastos fijos ni gastos diarios
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, outputStream);
            doc.open();

            doc.add(new Paragraph("TARJETAS CREDITO POR MES DEL USUARIO: "));

            for (TarjetaCredito tarjetaCredito : tarjetasCredito) {
                doc.add(new Paragraph("Tarjeta de Crédito: " + tarjetaCredito.getNombreTarjeta()));
                doc.add(new Paragraph("Fecha de Pago: " + tarjetaCredito.getFechaPago().toString()));
                doc.add(new Paragraph("Valor Total: " + tarjetaCredito.getValorTotal().toString()));
                doc.add(new Paragraph("\n"));

                List<GastoTarjeta> gastos = tarjetaCredito.getGastos();
                if (!gastos.isEmpty()) {
                    PdfPTable table = new PdfPTable(7); // 7 columnas para los datos de cada gasto
                    table.setWidthPercentage(100);
                    table.addCell("Nombre");
                    table.addCell("Cuota Total");
                    table.addCell("Cuota Actual");
                    table.addCell("Valor Cuota Gasto");
                    table.addCell("Valor Total Gasto");
                    table.addCell("Tipo");
                    table.addCell("Fecha");

                    for (GastoTarjeta gasto : gastos) {
                        table.addCell(gasto.getNombreGasto());
                        table.addCell(gasto.getCuotaTotal().toString());
                        table.addCell(gasto.getCuotaActual().toString());
                        table.addCell(gasto.getValorCuotaGasto().toString());
                        table.addCell(gasto.getValorTotalGasto().toString());
                        table.addCell(gasto.getTipo().toString());
                        table.addCell(gasto.getFecha().toString());
                    }

                    doc.add(table);
                }

                doc.add(new Paragraph("\n"));
            }
           

            doc.add(new Paragraph("GASTOS FIJOS POR MES DEL USUARIO: "));

            // Agregar gastos fijos
            for (GastoFijo gastoFijo : gastosFijos) {
                doc.add(new Paragraph("Gasto Fijo: " + gastoFijo.getNombreGasto()));
                doc.add(new Paragraph("Valor Total: " + gastoFijo.getValorTotal().toString()));
                doc.add(new Paragraph("Fecha: " + gastoFijo.getFecha().toString()));
                doc.add(new Paragraph("\n"));

                List<GastoInvFijo> gastosIndividuales = gastoFijo.getGastos();
                if (!gastosIndividuales.isEmpty()) {
                    PdfPTable table = new PdfPTable(4); // 4 columnas para los datos de cada gasto individual
                    table.setWidthPercentage(100);
                    table.addCell("Nombre");
                    table.addCell("Valor");
                    table.addCell("Fecha");
                    table.addCell("Tipo");

                    for (GastoInvFijo gastoIndividual : gastosIndividuales) {
                        table.addCell(gastoIndividual.getNombreGasto());
                        table.addCell(gastoIndividual.getValorGasto().toString());
                        table.addCell(gastoIndividual.getFecha().toString());
                        table.addCell(gastoIndividual.getTipo().toString());
                    }

                    doc.add(table);
                }

                doc.add(new Paragraph("\n"));
            }
              doc.add(new Paragraph("GASTOS VARIABLES POR MES DEL USUARIO: "));

    for (GastoVariable gastoVariable : gastosVariables) {
        doc.add(new Paragraph("Valor Total: " + gastoVariable.getValorTotal().toString()));
        doc.add(new Paragraph("Fecha: " + gastoVariable.getFecha().toString()));
        doc.add(new Paragraph("\n"));

        List<GastoVariableIndividual> gastosIndividuales = gastoVariable.getGastos();
        if (!gastosIndividuales.isEmpty()) {
            PdfPTable table = new PdfPTable(4); // 4 columnas para los datos de cada gasto individual
            table.setWidthPercentage(100);
            table.addCell("Nombre");
            table.addCell("Valor");
            table.addCell("Fecha");
            table.addCell("Tipo");

            for (GastoVariableIndividual gastoIndividual : gastosIndividuales) {
                table.addCell(gastoIndividual.getNombreGasto());
                table.addCell(gastoIndividual.getValorGasto().toString());
                table.addCell(gastoIndividual.getFecha().toString());
                table.addCell(gastoIndividual.getTipo().toString());
            }

            doc.add(table);
        }
        

        doc.add(new Paragraph("\n"));
        
    }

    
    doc.add(new Paragraph("GASTOS DIARIOS POR MES DEL USUARIO: "));
    for (GastoDiario gastoDiario : gastosDiarios) {
        doc.add(new Paragraph("Valor Total: " + gastoDiario.getValorTotal().toString()));
        doc.add(new Paragraph("Fecha: " + gastoDiario.getFecha().toString()));
        doc.add(new Paragraph("\n"));

        List<GastoDiarioIndividual> gastosIndividuales = gastoDiario.getGastos();
        if (!gastosIndividuales.isEmpty()) {
            PdfPTable table = new PdfPTable(4); // 4 columnas para los datos de cada gasto individual
            table.setWidthPercentage(100);
            table.addCell("Nombre");
            table.addCell("Valor");
            table.addCell("Fecha");
            table.addCell("Tipo");

            for (GastoDiarioIndividual gastoIndividual : gastosIndividuales) {
                table.addCell(gastoIndividual.getNombreGasto());
                table.addCell(gastoIndividual.getValorGasto().toString());
                table.addCell(gastoIndividual.getFecha().toString());
                table.addCell(gastoIndividual.getTipo().toString());
            }

            doc.add(table);

        }

        doc.add(new Paragraph("\n"));

    }

        } catch (DocumentException e) {
         
            e.printStackTrace();
        } finally {
            if (doc != null) {
                doc.close();
            }
        }

        return outputStream.toByteArray();
    }
}