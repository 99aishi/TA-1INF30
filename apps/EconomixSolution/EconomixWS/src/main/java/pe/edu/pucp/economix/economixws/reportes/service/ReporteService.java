package pe.edu.pucp.economix.economixws.reportes.service;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import pe.edu.pucp.economix.config.DBManager;

public class ReporteService {

    public byte[] generarReporteGastosPorArea(Date fechaInicio, Date fechaFin) throws Exception {
        InputStream reporteStream = getClass().getClassLoader()
                .getResourceAsStream("reportes/GastosPorArea.jasper");

        if (reporteStream == null) {
            throw new Exception("No se encontró el archivo del reporte: reportes/GastosPorArea.jasper");
        }

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("FECHA_INICIO", new java.sql.Date(fechaInicio.getTime()));
        parametros.put("FECHA_FIN", new java.sql.Date(fechaFin.getTime()));

        Connection con = DBManager.getDBManager().getConnection();

        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(reporteStream, parametros, con);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } finally {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        }
    }
}
