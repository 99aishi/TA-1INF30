package pe.edu.pucp.economix.economixws.tesoreria.ws;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.economix.tesoreria.boi.MonedaBOImpl;
import pe.edu.pucp.economix.tesoreria.ibo.IMonedaBO;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.util.List;
import java.util.Map;

@Path("MonedaWS")
public class MonedaWS {
    private IMonedaBO monedaBO = new MonedaBOImpl();

    @GET
    @Path("ListarMonedas")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Moneda> listarMonedas() throws Exception {
        return monedaBO.listarTodas();
    }

    @GET
    @Path("BuscarPorId")
    @Produces(MediaType.APPLICATION_JSON)
    public Moneda buscarPorId(@QueryParam("id") int id) throws Exception {
        return monedaBO.buscarPorId(id);
    }

    @GET
    @Path("BuscarMonedas")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Moneda> buscarMonedas(@QueryParam("q") String q) throws Exception {
        return monedaBO.listarMonedas_X_codigoISO_nombre_simbolo(q);
    }

    @GET
    @Path("ListarPorEstado")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Moneda> listarPorEstado(@QueryParam("activa") boolean activa) throws Exception {
        return monedaBO.listarMonedas_X_estado(activa);
    }

    @POST
    @Path("Insertar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertarMoneda(Moneda moneda, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int id = monedaBO.insertar(moneda, idUsuarioAccion);
            return Response.ok(id).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("Actualizar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizarMoneda(Moneda moneda, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int result = monedaBO.modificar(moneda, idUsuarioAccion);
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("Eliminar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarMoneda(@QueryParam("id") int id, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int result = monedaBO.eliminar(id, idUsuarioAccion);
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("ListarActivas")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Moneda> listarActivas() throws Exception {
        return monedaBO.listarActivas();
    }

    @GET
    @Path("Recuperar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response recuperarMoneda(@QueryParam("id") int id, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int result = monedaBO.recuperar(id, idUsuarioAccion);
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }
}
