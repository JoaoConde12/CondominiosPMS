package com.condominiospms.CondominiosPMS.servicios;

import com.condominiospms.CondominiosPMS.modelo.Alicuota;
import com.condominiospms.CondominiosPMS.modelo.Pago;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GeneradorPDFService {

    private static final String DIRECTORIO_PDF = "/tmp/condominios/comprobantes/";
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

    public static String generarComprobante(Pago pago) throws Exception {
        new File(DIRECTORIO_PDF).mkdirs();

        String nombreArchivo = "comprobante_" + pago.getId() + "_" +
                System.currentTimeMillis() + ".pdf";
        String rutaCompleta = DIRECTORIO_PDF + nombreArchivo;

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(rutaCompleta));
        document.open();

        Font fontTitulo = new Font(Font.HELVETICA, 18, Font.BOLD);
        Font fontSubtitulo = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font fontNormal = new Font(Font.HELVETICA, 11, Font.NORMAL);
        Font fontBold = new Font(Font.HELVETICA, 11, Font.BOLD);

        Paragraph titulo = new Paragraph("CONDOMINIOS PMS", fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        Paragraph subtitulo = new Paragraph("COMPROBANTE DE PAGO", fontSubtitulo);
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        document.add(subtitulo);

        document.add(new Paragraph(" "));

        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(100);

        agregarFila(tabla, "Código:", "COMP-" + pago.getId(), fontBold, fontNormal);
        agregarFila(tabla, "Fecha de Pago:",
                pago.getFechaPago() != null ? SDF.format(pago.getFechaPago()) : "-",
                fontBold, fontNormal);
        agregarFila(tabla, "Monto:", "$ " + pago.getMonto(), fontBold, fontNormal);
        agregarFila(tabla, "Método:",
                pago.getMetodo() != null ? pago.getMetodo().name() : "-",
                fontBold, fontNormal);
        agregarFila(tabla, "Referencia:",
                pago.getReferenciaBancaria() != null ? pago.getReferenciaBancaria() : "-",
                fontBold, fontNormal);
        agregarFila(tabla, "Estado:",
                pago.getEstado() != null ? pago.getEstado().name() : "-",
                fontBold, fontNormal);

        if (pago.getAlicuota() != null) {
            agregarFila(tabla, "Unidad:",
                    pago.getAlicuota().getUnidad() != null ?
                            pago.getAlicuota().getUnidad().getCodigo() : "-",
                    fontBold, fontNormal);
            agregarFila(tabla, "Período:",
                    pago.getAlicuota().getPeriodoMes() + "/" +
                            pago.getAlicuota().getPeriodoAnio(),
                    fontBold, fontNormal);
        }

        document.add(tabla);

        document.add(new Paragraph(" "));
        Paragraph pie = new Paragraph(
                "Generado el " + SDF.format(new Date()) + " - Condominios PMS",
                new Font(Font.HELVETICA, 9, Font.ITALIC));
        pie.setAlignment(Element.ALIGN_CENTER);
        document.add(pie);

        document.close();
        return rutaCompleta;
    }

    public static String generarReporteMorosidad(List<Alicuota> alicuotas)
            throws Exception {
        new File(DIRECTORIO_PDF).mkdirs();
        String nombreArchivo = "reporte_morosidad_" +
                System.currentTimeMillis() + ".pdf";
        String rutaCompleta = DIRECTORIO_PDF + nombreArchivo;

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(rutaCompleta));
        document.open();

        Font fontTitulo = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font fontNormal = new Font(Font.HELVETICA, 10, Font.NORMAL);
        Font fontHeader = new Font(Font.HELVETICA, 10, Font.BOLD);

        Paragraph titulo = new Paragraph("REPORTE DE MOROSIDAD", fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        Paragraph fecha = new Paragraph("Generado: " + SDF.format(new Date()),
                fontNormal);
        fecha.setAlignment(Element.ALIGN_RIGHT);
        document.add(fecha);

        document.add(new Paragraph(" "));

        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{2f, 1f, 1f, 1.5f, 1.5f});

        String[] headers = {"Unidad", "Período", "Monto Total",
                "Monto Pagado", "Estado"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, fontHeader));
            cell.setBackgroundColor(new java.awt.Color(200, 200, 200));
            cell.setPadding(5);
            tabla.addCell(cell);
        }

        for (Alicuota alicuota : alicuotas) {
            tabla.addCell(new Phrase(
                    alicuota.getUnidad() != null ?
                            alicuota.getUnidad().getCodigo() : "-", fontNormal));
            tabla.addCell(new Phrase(
                    alicuota.getPeriodoMes() + "/" +
                            alicuota.getPeriodoAnio(), fontNormal));
            tabla.addCell(new Phrase(
                    "$ " + alicuota.getMontoTotal(), fontNormal));
            tabla.addCell(new Phrase(
                    "$ " + alicuota.getMontoPagado(), fontNormal));
            tabla.addCell(new Phrase(
                    alicuota.getEstado() != null ?
                            alicuota.getEstado().name() : "-", fontNormal));
        }

        document.add(tabla);
        document.close();
        return rutaCompleta;
    }

    private static void agregarFila(PdfPTable tabla, String label,
                                    String valor, Font fontLabel, Font fontValor) {
        PdfPCell celda1 = new PdfPCell(new Phrase(label, fontLabel));
        celda1.setPadding(5);
        PdfPCell celda2 = new PdfPCell(new Phrase(valor, fontValor));
        celda2.setPadding(5);
        tabla.addCell(celda1);
        tabla.addCell(celda2);
    }
}