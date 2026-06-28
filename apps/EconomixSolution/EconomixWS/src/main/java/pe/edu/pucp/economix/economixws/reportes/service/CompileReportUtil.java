package pe.edu.pucp.economix.economixws.reportes.service;

import net.sf.jasperreports.engine.JasperCompileManager;

public class CompileReportUtil {
    public static void main(String[] args) throws Exception {
        String jrxml = "src/main/resources/reportes/ReporteFinalGastosPorArea.jrxml";
        String jasper = "src/main/resources/reportes/ReporteFinalGastosPorArea.jasper";
        System.out.println("Compilando: " + jrxml);
        JasperCompileManager.compileReportToFile(jrxml, jasper);
        System.out.println("Reporte compilado: " + jasper);
    }
}