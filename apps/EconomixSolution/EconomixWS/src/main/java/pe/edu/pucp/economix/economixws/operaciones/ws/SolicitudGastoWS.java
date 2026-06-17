package pe.edu.pucp.economix.economixws.operaciones.ws;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.economix.operaciones.boi.SolicitudGastoBOImpl;
import pe.edu.pucp.economix.operaciones.ibo.ISolicitudGastoBO;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;

import java.util.List;
import java.util.Map;

@Path("SolicitudGastoWS")
public class SolicitudGastoWS {
    private ISolicitudGastoBO solicitudBO = new SolicitudGastoBOImpl();

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
                            @QueryParam("aprobado") boolean aprobado,
                            @QueryParam("comentario") String comentario,
                            @QueryParam("idJefeEvaluador") int idJefeEvaluador,
                            @QueryParam("numeroOperacionBancaria") String numeroOperacionBancaria,
                            @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int r = solicitudBO.evaluar(idSolicitudGasto, aprobado, comentario, idJefeEvaluador, numeroOperacionBancaria, idUsuarioAccion);
            return Response.ok(r).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }
}
