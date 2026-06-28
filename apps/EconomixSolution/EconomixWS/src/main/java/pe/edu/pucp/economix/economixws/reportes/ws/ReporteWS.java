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
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("reportes")
public class ReporteWS {

    private static final Logger LOG = Logger.getLogger(ReporteWS.class.getName());
    private final ReporteService reporteService = new ReporteService();

    @GET
    @Path("gastos-por-area")
    @Produces("application/pdf")
    public Response generarReporteGastosPorArea(
            @QueryParam("fechaInicio") String fechaInicioStr,
            @QueryParam("fechaFin") String fechaFinStr,
            @QueryParam("idsAreas") String idsAreas) {
        try {
            if (fechaInicioStr == null || fechaInicioStr.isBlank()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", "El parámetro fechaInicio es obligatorio"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
            if (fechaFinStr == null || fechaFinStr.isBlank()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", "El parámetro fechaFin es obligatorio"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
            if (idsAreas == null || idsAreas.isBlank()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", "El parámetro idsAreas es obligatorio"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
            if (!idsAreas.matches("\\d+(,\\d+)*")) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", "idsAreas debe ser una lista de números separados por coma (ej: 1,2,3)"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaInicio = sdf.parse(fechaInicioStr);
            Date fechaFin = sdf.parse(fechaFinStr);

            if (fechaInicio.after(fechaFin)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", "La fecha de inicio no puede ser posterior a la fecha fin"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            byte[] pdf = reporteService.generarReporteGastosPorArea(fechaInicio, fechaFin, idsAreas);

            return Response.ok(pdf)
                    .header("Content-Disposition", "inline; filename=\"ReporteGastosPorArea.pdf\"")
                    .type("application/pdf")
                    .build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al generar reporte gastos-por-area", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage() != null ? e.getMessage() : "Error interno del servidor"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}