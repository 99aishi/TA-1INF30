package pe.edu.pucp.economix.economixws.reportes.ws;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.economix.economixws.reportes.service.ReporteService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Path("reportes")
public class ReporteWS {

    private final ReporteService reporteService = new ReporteService();

    @GET
    @Path("gastos-de-areas")
    @Produces("application/pdf")
    public Response generarReporteGastosDeAreas(
            @QueryParam("fechaInicio") String fechaInicioStr,
            @QueryParam("fechaFin") String fechaFinStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaInicio = sdf.parse(fechaInicioStr);
            Date fechaFin = sdf.parse(fechaFinStr);

            byte[] pdf = reporteService.generarReporteGastosDeAreas(fechaInicio, fechaFin);

            return Response.ok(pdf)
                    .header("Content-Disposition", "inline; filename=\"ReporteGastosDeAreas.pdf\"")
                    .type("application/pdf")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @GET
    @Path("solicitudes-gasto")
    @Produces("application/pdf")
    public Response generarReporteSolicitudesGasto(
            @QueryParam("nombreArea") String nombreArea,
            @QueryParam("fechaInicio") String fechaInicioStr,
            @QueryParam("fechaFin") String fechaFinStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaInicio = sdf.parse(fechaInicioStr);
            Date fechaFin = sdf.parse(fechaFinStr);

            byte[] pdf = reporteService.generarReporteSolicitudesGasto(nombreArea, fechaInicio, fechaFin);

            return Response.ok(pdf)
                    .header("Content-Disposition", "inline; filename=\"ReporteSolicitudesGasto.pdf\"")
                    .type("application/pdf")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}
