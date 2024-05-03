package co.finanplus.api.service;


import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;
import com.itextpdf.text.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.Font;
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
import co.finanplus.api.domain.usuarios.Usuario;
import co.finanplus.api.domain.usuarios.UsuarioRepository;

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
    private UsuarioRepository usuarioRepository;

    @Autowired
    private GastoDiarioRepository gastoDiarioRepository;

    private String obtenerNombreMes(LocalDate fecha) {
        return fecha.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
    }

    private Map<LocalDate, List<Object>> agruparGastosPorMes(List<TarjetaCredito> tarjetasCredito,
            List<GastoFijo> gastosFijos, List<GastoVariable> gastosVariables, List<GastoDiario> gastosDiarios) {
        Map<LocalDate, List<Object>> gastosPorMes = new LinkedHashMap<>(); // Usar LinkedHashMap para mantener el orden
                                                                           // de inserción

        // Crear un Stream con los meses desde enero hasta diciembre
        Stream.iterate(LocalDate.of(LocalDate.now().getYear(), 1, 1), date -> date.plusMonths(1))
                .limit(12)
                .forEach(mes -> {
                    gastosPorMes.put(mes, new ArrayList<>());
                });

        // Agregar tarjetas de crédito
        for (TarjetaCredito tarjeta : tarjetasCredito) {
            LocalDate mes = tarjeta.getFecha().withDayOfMonth(1);
            List<Object> gastosDelMes = gastosPorMes.getOrDefault(mes, new ArrayList<>());
            gastosDelMes.add(tarjeta);
            gastosPorMes.put(mes, gastosDelMes);
        }

        // Agregar gastos fijos
        for (GastoFijo gasto : gastosFijos) {
            LocalDate mes = gasto.getFecha().withDayOfMonth(1);
            List<Object> gastosDelMes = gastosPorMes.getOrDefault(mes, new ArrayList<>());
            gastosDelMes.add(gasto);
            gastosPorMes.put(mes, gastosDelMes);
        }

        // Agregar gastos variables
        for (GastoVariable gasto : gastosVariables) {
            LocalDate mes = gasto.getFecha().withDayOfMonth(1);
            List<Object> gastosDelMes = gastosPorMes.getOrDefault(mes, new ArrayList<>());
            gastosDelMes.add(gasto);
            gastosPorMes.put(mes, gastosDelMes);
        }

        // Agregar gastos diarios
        for (GastoDiario gasto : gastosDiarios) {
            LocalDate mes = gasto.getFecha().withDayOfMonth(1);
            List<Object> gastosDelMes = gastosPorMes.getOrDefault(mes, new ArrayList<>());
            gastosDelMes.add(gasto);
            gastosPorMes.put(mes, gastosDelMes);
        }

        return gastosPorMes;
    }

    public byte[] generarPDF(String userId) {
        List<TarjetaCredito> tarjetasCredito = tarjetaCreditoRepository.findByUsuarioIDOrderByFecha(userId);
        List<GastoFijo> gastosFijos = gastoFijoRepository.findByUsuarioIDOrderByFecha(userId);

        List<GastoVariable> gastosVariables = gastoVariableRepository.findByUsuarioIDOrderByFecha(userId);
        List<GastoDiario> gastosDiarios = gastoDiarioRepository.findByUsuarioIDOrderByFecha(userId);
        Usuario usuario = usuarioRepository.findById(userId).orElse(null);

        if (tarjetasCredito.isEmpty() && gastosFijos.isEmpty() && gastosDiarios.isEmpty()
                && gastosVariables.isEmpty()) {
            // Manejo de error: usuario sin tarjetas de crédito, gastos fijos ni gastos
            // diarios
            return null;
        }

        String nombreUsuario = usuario.getNombre();
        String emailUsuario = usuario.getEmail();

        String urlFotoPerfil = usuario.getPhotoUrl();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, outputStream);
            doc.open();

            doc.add(new Paragraph("Usuario: " + nombreUsuario, new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD)));
            doc.add(new Paragraph("Email: " + emailUsuario));

            Map<LocalDate, List<Object>> gastosPorMes = agruparGastosPorMes(tarjetasCredito, gastosFijos,
                    gastosVariables, gastosDiarios);

            for (Map.Entry<LocalDate, List<Object>> entry : gastosPorMes.entrySet()) {
                LocalDate mes = entry.getKey();
                List<Object> gastosDelMes = entry.getValue();

                doc.add(new Paragraph("Gastos del mes " + obtenerNombreMes(mes) + ":"));

                for (Object gasto : gastosDelMes) {
                    if (gasto instanceof TarjetaCredito) {
                        TarjetaCredito tarjetaCredito = (TarjetaCredito) gasto;
                        doc.add(new Paragraph("Tarjeta de Crédito: " + tarjetaCredito.getNombreTarjeta()));
                        doc.add(new Paragraph("Fecha de Pago: " + tarjetaCredito.getFechaPago().toString()));
                        doc.add(new Paragraph("Valor Total: " + tarjetaCredito.getValorTotal().toString()));
                        doc.add(new Paragraph("\n"));

                        List<GastoTarjeta> gastosTC = tarjetaCredito.getGastos();
                        if (!gastosTC.isEmpty()) {
                            PdfPTable table = new PdfPTable(7); // 7 columnas para los datos de cada gasto
                            table.setWidthPercentage(100);
                            table.addCell("Nombre");
                            table.addCell("Cuota Total");
                            table.addCell("Cuota Actual");
                            table.addCell("Valor Cuota Gasto");
                            table.addCell("Valor Total Gasto");
                            table.addCell("Tipo");
                            table.addCell("Fecha");

                            for (GastoTarjeta gastoTC : gastosTC) {
                                table.addCell(gastoTC.getNombreGasto());
                                table.addCell(gastoTC.getCuotaTotal().toString());
                                table.addCell(gastoTC.getCuotaActual().toString());
                                table.addCell(gastoTC.getValorCuotaGasto().toString());
                                table.addCell(gastoTC.getValorTotalGasto().toString());
                                table.addCell(gastoTC.getTipo().toString());
                                table.addCell(gastoTC.getFecha().toString());
                            }

                            doc.add(table);
                        }

                        doc.add(new Paragraph("\n"));
                    } else if (gasto instanceof GastoFijo) {
                        doc.add(new Paragraph("Gasto Fijos del mes: "));
                        GastoFijo gastoFijo = (GastoFijo) gasto;
                        doc.add(new Paragraph("Nombre del gasto fijo: " + gastoFijo.getNombreGasto()));
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
                    } else if (gasto instanceof GastoVariable) {
                        doc.add(new Paragraph("Gasto Variables del mes: "));
                        GastoVariable gastoVariable = (GastoVariable) gasto;
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

                    } else if (gasto instanceof GastoDiario) {
                        doc.add(new Paragraph("Gastos Diarios del mes: " , new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD)));
                        GastoDiario gastoDiario = (GastoDiario) gasto;
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