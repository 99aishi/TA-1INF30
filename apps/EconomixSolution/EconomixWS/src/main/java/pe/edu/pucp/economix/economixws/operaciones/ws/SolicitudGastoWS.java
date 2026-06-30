package pe.edu.pucp.economix.economixws.operaciones.ws;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.economix.operaciones.boi.SolicitudGastoBOImpl;
import pe.edu.pucp.economix.operaciones.ibo.ISolicitudGastoBO;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Path("SolicitudGastoWS")
public class SolicitudGastoWS {
    private ISolicitudGastoBO solicitudBO = new SolicitudGastoBOImpl();

    @GET
    @Path("HorarioHabilitado")
    @Produces(MediaType.APPLICATION_JSON)
    public Response horarioHabilitado() {
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int timeInMinutes = hour * 60 + minute;

        boolean enVentana;
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            enVentana = false;
        } else if (dayOfWeek == Calendar.MONDAY) {
            enVentana = timeInMinutes >= 8 * 60;
        } else if (dayOfWeek == Calendar.FRIDAY) {
            enVentana = timeInMinutes < 18 * 60;
        } else {
            enVentana = true;
        }

        String mensaje;
        if (!enVentana) {
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                mensaje = "Las solicitudes solo pueden registrarse de lunes a viernes.";
            } else if (dayOfWeek == Calendar.MONDAY) {
                mensaje = "Las solicitudes se habilitan desde el lunes a las 8:00 AM.";
            } else {
                mensaje = "Las solicitudes cierran el viernes a las 6:00 PM.";
            }
        } else {
            mensaje = "Horario habilitado para registro de solicitudes.";
        }

        return Response.ok(Map.of("habilitado", enVentana, "mensaje", mensaje)).build();
    }

    @GET
    @Path("Listar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SolicitudGasto> listar() throws Exception {
        return solicitudBO.listarTodas();
    }

    @GET
    @Path("BuscarPorId")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(@QueryParam("id") int id) {
        try {
            SolicitudGasto sg = solicitudBO.buscarPorId(id);
            if (sg == null)
                return Response.status(Response.Status.NO_CONTENT).build();
            return Response.ok(sg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Path("ListarPorSolicitante")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SolicitudGasto> listarPorSolicitante(@QueryParam("idSolicitante") int id) throws Exception {
        return solicitudBO.listarPorSolicitante(id);
    }

    @GET
    @Path("ListarPendientesJefe")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SolicitudGasto> listarPendientesJefe(@QueryParam("idJefe") int id) throws Exception {
        return solicitudBO.listarPendientesJefe(id);
    }

    @GET
    @Path("ListarPorCiclo")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SolicitudGasto> listarPorCiclo(@QueryParam("idCiclo") int id) throws Exception {
        return solicitudBO.listarPorCiclo(id);
    }

    @POST
    @Path("Insertar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertar(SolicitudGasto sg, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int id = solicitudBO.insertar(sg, idUsuarioAccion);
            return Response.ok(id).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("Actualizar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizar(SolicitudGasto sg, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int r = solicitudBO.modificar(sg, idUsuarioAccion);
            return Response.ok(r).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Path("Eliminar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminar(@QueryParam("id") int id, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int r = solicitudBO.eliminar(id, idUsuarioAccion);
            return Response.ok(r).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("Evaluar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response evaluar(@QueryParam("idSolicitudGasto") int idSolicitudGasto,
                            @QueryParam("accion") String accion,
                            @QueryParam("comentario") String comentario,
                            @QueryParam("idJefeEvaluador") int idJefeEvaluador,
                            @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            SolicitudGasto solicitud = solicitudBO.evaluar(idSolicitudGasto, accion, comentario, idJefeEvaluador, idUsuarioAccion);
            return Response.ok(solicitud).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }
}
