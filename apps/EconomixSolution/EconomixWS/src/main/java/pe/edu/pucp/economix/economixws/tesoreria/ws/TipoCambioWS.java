package pe.edu.pucp.economix.economixws.tesoreria.ws;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.economix.tesoreria.boi.TipoCambioBOImpl;
import pe.edu.pucp.economix.tesoreria.ibo.ITipoCambioBO;
import pe.edu.pucp.economix.tesoreria.model.TipoCambio;

import java.util.List;
import java.util.Map;

@Path("TipoCambioWS")
public class TipoCambioWS {
    private final ITipoCambioBO tipoCambioBO = new TipoCambioBOImpl();

    @GET
    @Path("Listar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TipoCambio> listar() throws Exception {
        return tipoCambioBO.listarTodas();
    }

    @GET
    @Path("BuscarPorId")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(@QueryParam("id") int id) {
        try {
            TipoCambio tc = tipoCambioBO.buscarPorId(id);
            if (tc == null)
                return Response.status(Response.Status.NO_CONTENT).build();
            return Response.ok(tc).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Path("BuscarPorMonedasYFecha")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorMonedasYFecha(@QueryParam("idMonedaOrigen") int idMonedaOrigen,
                                            @QueryParam("idMonedaDestino") int idMonedaDestino,
                                            @QueryParam("fecha") String fecha) {
        try {
            java.util.Date fechaDate = fecha == null || fecha.trim().isEmpty()
                    ? new java.util.Date()
                    : java.sql.Date.valueOf(fecha);
            TipoCambio tc = tipoCambioBO.buscarPorMonedasYFecha(idMonedaOrigen, idMonedaDestino, fechaDate);
            if (tc == null)
                return Response.status(Response.Status.NO_CONTENT).build();
            return Response.ok(tc).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("Insertar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertar(TipoCambio tc, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int id = tipoCambioBO.insertar(tc, idUsuarioAccion);
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
    public Response actualizar(TipoCambio tc, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int r = tipoCambioBO.modificar(tc, idUsuarioAccion);
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
            int r = tipoCambioBO.eliminar(id, idUsuarioAccion);
            return Response.ok(r).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }
}
