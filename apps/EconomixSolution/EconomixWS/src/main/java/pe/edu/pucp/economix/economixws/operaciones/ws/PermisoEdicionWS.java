package pe.edu.pucp.economix.economixws.operaciones.ws;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.economix.operaciones.boi.PermisoEdicionBOImpl;
import pe.edu.pucp.economix.operaciones.ibo.IPermisoEdicionBO;
import pe.edu.pucp.economix.operaciones.model.PermisoEdicion;

import java.util.List;
import java.util.Map;

@Path("PermisoEdicionWS")
public class PermisoEdicionWS {
    private IPermisoEdicionBO permisoBO = new PermisoEdicionBOImpl();

    @POST
    @Path("Solicitar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response solicitar(PermisoEdicion permiso, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int id = permisoBO.solicitar(permiso, idUsuarioAccion);
            return Response.ok(id).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("Otorgar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response otorgar(@QueryParam("idPermiso") int idPermiso,
                            @QueryParam("idAutorizador") int idAutorizador,
                            @QueryParam("motivoAutorizacion") String motivoAutorizacion,
                            @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int r = permisoBO.otorgar(idPermiso, idAutorizador, motivoAutorizacion, idUsuarioAccion);
            return Response.ok(r).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("Revocar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response revocar(@QueryParam("idPermiso") int idPermiso, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int r = permisoBO.revocar(idPermiso, idUsuarioAccion);
            return Response.ok(r).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Path("ListarPendientes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PermisoEdicion> listarPendientes(@QueryParam("idAutorizador") int idAutorizador) throws Exception {
        return permisoBO.listarPendientes(idAutorizador);
    }

    @GET
    @Path("ListarEnExcepcion")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PermisoEdicion> listarEnExcepcion() throws Exception {
        return permisoBO.listarComprobantesEnExcepcion();
    }
}
