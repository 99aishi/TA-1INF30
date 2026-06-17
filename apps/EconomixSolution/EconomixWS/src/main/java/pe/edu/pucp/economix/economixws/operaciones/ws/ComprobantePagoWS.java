package pe.edu.pucp.economix.economixws.operaciones.ws;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.economix.operaciones.boi.ComprobantePagoBOImpl;
import pe.edu.pucp.economix.operaciones.ibo.IComprobantePagoBO;
import pe.edu.pucp.economix.operaciones.model.ComprobantePago;

import java.util.List;
import java.util.Map;

@Path("ComprobantePagoWS")
public class ComprobantePagoWS {
    private IComprobantePagoBO comprobanteBO = new ComprobantePagoBOImpl();

    @GET
    @Path("Listar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ComprobantePago> listar() throws Exception {
        return comprobanteBO.listarTodas();
    }

    @GET
    @Path("BuscarPorId")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(@QueryParam("id") int id) {
        try {
            ComprobantePago cp = comprobanteBO.buscarPorId(id);
            if (cp == null)
                return Response.status(Response.Status.NO_CONTENT).build();
            return Response.ok(cp).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Path("ListarPorSolicitud")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ComprobantePago> listarPorSolicitud(@QueryParam("idSolicitud") int id) throws Exception {
        return comprobanteBO.listarPorSolicitud(id);
    }

    @POST
    @Path("Insertar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertar(ComprobantePago cp, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int id = comprobanteBO.insertar(cp, idUsuarioAccion);
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
    public Response actualizar(ComprobantePago cp, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int r = comprobanteBO.modificar(cp, idUsuarioAccion);
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
            int r = comprobanteBO.eliminar(id, idUsuarioAccion);
            return Response.ok(r).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("Evaluar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response evaluar(@QueryParam("idComprobante") int idComprobante,
                            @QueryParam("aprobar") boolean aprobar,
                            @QueryParam("observacion") String observacion,
                            @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int r = comprobanteBO.evaluar(idComprobante, aprobar, observacion, idUsuarioAccion);
            return Response.ok(r).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }
}
