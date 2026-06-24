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
    @Path("gastos-por-area")
    @Produces("application/pdf")
    public Response generarReporteGastosPorArea(
            @QueryParam("fechaInicio") String fechaInicioStr,
            @QueryParam("fechaFin") String fechaFinStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaInicio = sdf.parse(fechaInicioStr);
            Date fechaFin = sdf.parse(fechaFinStr);

            byte[] pdf = reporteService.generarReporteGastosPorArea(fechaInicio, fechaFin);

            return Response.ok(pdf)
                    .header("Content-Disposition", "inline; filename=\"ReporteGastosPorArea.pdf\"")
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
