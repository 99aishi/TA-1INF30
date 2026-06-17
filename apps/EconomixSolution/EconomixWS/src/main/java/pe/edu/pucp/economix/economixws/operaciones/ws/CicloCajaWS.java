package pe.edu.pucp.economix.economixws.operaciones.ws;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.economix.operaciones.boi.CicloCajaBOImpl;
import pe.edu.pucp.economix.operaciones.ibo.ICicloCajaBO;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;

import java.util.List;
import java.util.Map;

@Path("CicloCajaWS")
public class CicloCajaWS {
    private ICicloCajaBO cicloBO = new CicloCajaBOImpl();

    @GET
    @Path("Listar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CicloCajaChica> listar() throws Exception {
        return cicloBO.listarTodas();
    }

    @GET
    @Path("BuscarPorId")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(@QueryParam("id") int id) {
        try {
            CicloCajaChica cc = cicloBO.buscarPorId(id);
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
    public Response insertar(CicloCajaChica ciclo, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int id = cicloBO.insertar(ciclo, idUsuarioAccion);
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
    public Response actualizar(CicloCajaChica ciclo, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int r = cicloBO.modificar(ciclo, idUsuarioAccion);
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
            int r = cicloBO.eliminar(id, idUsuarioAccion);
            return Response.ok(r).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Path("CalcularTotalGastado")
    @Produces(MediaType.APPLICATION_JSON)
    public Response calcularTotalGastado(@QueryParam("id") int id, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            CicloCajaChica cc = cicloBO.buscarPorId(id);
            if (cc == null)
                return Response.status(Response.Status.NO_CONTENT).build();
            cicloBO.calcularTotalGastado(cc, idUsuarioAccion);
            return Response.ok(cc.getTotalGastado()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("CerrarCiclo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cerrarCiclo(@QueryParam("id") int id, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int r = cicloBO.cerrarCiclo(id, idUsuarioAccion);
            return Response.ok(r).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }
}
