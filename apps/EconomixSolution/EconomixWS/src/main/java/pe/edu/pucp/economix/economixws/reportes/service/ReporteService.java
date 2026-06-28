package pe.edu.pucp.economix.economixws.reportes.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import pe.edu.pucp.economix.config.DBManager;

public class ReporteService {

    private static final Logger LOG = Logger.getLogger(ReporteService.class.getName());

    public byte[] generarReporteGastosPorArea(Date fechaInicio, Date fechaFin, String idsAreas) throws Exception {
        String ruta = "reportes/ReporteFinalGastosPorArea.jasper";
        InputStream reporteStream = getClass().getClassLoader().getResourceAsStream(ruta);

        if (reporteStream == null) {
            LOG.severe("No se encontró el reporte en classpath: " + ruta);
            throw new Exception("No se encontró el archivo del reporte: " + ruta);
        }

        byte[] buffer;
        try {
            buffer = reporteStream.readAllBytes();
        } finally {
            reporteStream.close();
        }

        LOG.info("Reporte cargado: " + ruta + " (" + buffer.length + " bytes)");

        if (buffer.length == 0) {
            throw new Exception("El archivo del reporte está vacío: " + ruta);
        }

        JasperReport jasperReport;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            jasperReport = (JasperReport) JRLoader.loadObject(bais);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al cargar JasperReport (" + buffer.length + " bytes)", e);
            throw new Exception("Error al cargar el reporte Jasper: " + e.getMessage(), e);
        }

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("FECHA_INICIO", new java.sql.Date(fechaInicio.getTime()));
        parametros.put("FECHA_FIN", new java.sql.Date(fechaFin.getTime()));
        parametros.put("IDS_AREAS", idsAreas != null ? idsAreas : "");

        Connection con = DBManager.getDBManager().getConnection();
        try {
            LOG.info("Ejecutando reporte: FECHA_INICIO=" + fechaInicio + ", FECHA_FIN=" + fechaFin + ", IDS_AREAS=" + idsAreas);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, con);
            byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
            LOG.info("PDF generado: " + pdf.length + " bytes");
            return pdf;
        } catch (Exception e) {
            StringBuilder msg = new StringBuilder("Error al llenar/exportar reporte");
            Throwable cause = e;
            while (cause != null) {
                msg.append(" | ").append(cause.getClass().getSimpleName()).append(": ").append(cause.getMessage());
                cause = cause.getCause();
            }
            LOG.log(Level.SEVERE, msg.toString(), e);
            throw new Exception("Error al generar el PDF: " + msg, e);
        } finally {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        }
    }
}