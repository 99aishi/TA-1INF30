package pe.edu.pucp.economix.economixws.operaciones.ws;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.economix.operaciones.boi.TransaccionBOImpl;
import pe.edu.pucp.economix.operaciones.ibo.ITransaccionBO;
import pe.edu.pucp.economix.operaciones.model.Transaccion;

import java.util.List;
import java.util.Map;

@Path("TransaccionWS")
public class TransaccionWS {
    private ITransaccionBO transaccionBO = new TransaccionBOImpl();

    @GET
    @Path("Listar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Transaccion> listar() throws Exception {
        return transaccionBO.listarTodas();
    }

    @GET
    @Path("ListarActivas")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Transaccion> listarActivas() throws Exception {
        return transaccionBO.listarActivas();
    }

    @GET
    @Path("BuscarPorId")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(@QueryParam("id") int id) {
        try {
            Transaccion t = transaccionBO.buscarPorId(id);
            if (t == null)
                return Response.status(Response.Status.NO_CONTENT).build();
            return Response.ok(t).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("Insertar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertar(Transaccion t, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int id = transaccionBO.insertar(t, idUsuarioAccion);
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
    public Response actualizar(Transaccion t, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int r = transaccionBO.modificar(t, idUsuarioAccion);
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
            int r = transaccionBO.eliminar(id, idUsuarioAccion);
            return Response.ok(r).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Path("ListarPorJefe")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarPorJefe(@QueryParam("idJefe") int idJefe) {
        try {
            List<Transaccion> transacciones = transaccionBO.listarPorJefe(idJefe);
            if (transacciones == null || transacciones.isEmpty())
                return Response.status(Response.Status.NO_CONTENT).build();
            return Response.ok(transacciones).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }
}
