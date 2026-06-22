package pe.edu.pucp.economix.economixws.tesoreria.ws;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.economix.tesoreria.boi.CajaChicaBOImpl;
import pe.edu.pucp.economix.tesoreria.ibo.ICajaChicaBO;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;

import java.util.List;
import java.util.Map;

@Path("CajaChicaWS")
public class CajaChicaWS {
    private ICajaChicaBO cajaChicaBO = new CajaChicaBOImpl();

    @GET
    @Path("Listar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CajaChica> listar() throws Exception {
        return cajaChicaBO.listarTodas();
    }

    @GET
    @Path("BuscarPorId")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(@QueryParam("id") int id) {
        try {
            CajaChica cc = cajaChicaBO.buscarPorId(id);
            if (cc == null)
                return Response.status(Response.Status.NO_CONTENT).build();
            return Response.ok(cc).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("Insertar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertar(CajaChica cajaChica, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int id = cajaChicaBO.insertar(cajaChica, idUsuarioAccion);
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
    public Response actualizar(CajaChica cajaChica, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int r = cajaChicaBO.modificar(cajaChica, idUsuarioAccion);
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
            int r = cajaChicaBO.eliminar(id, idUsuarioAccion);
            return Response.ok(r).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Path("ListarPorCuentaBancaria")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarPorCuentaBancaria(@QueryParam("idCuentaBancaria") int idCuentaBancaria) {
        try {
            List<CajaChica> cajas = cajaChicaBO.listarPorCuentaBancaria(idCuentaBancaria);
            if (cajas == null || cajas.isEmpty())
                return Response.status(Response.Status.NO_CONTENT).build();
            return Response.ok(cajas).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }
}
