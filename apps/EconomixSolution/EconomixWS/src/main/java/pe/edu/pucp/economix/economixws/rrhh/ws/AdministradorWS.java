package pe.edu.pucp.economix.economixws.rrhh.ws;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.economix.rrhh.boi.AdministradorBOImpl;
import pe.edu.pucp.economix.rrhh.ibo.IAdministradorBO;
import pe.edu.pucp.economix.rrhh.model.Administrador;

import java.util.List;
import java.util.Map;

@Path("AdministradorWS")
public class AdministradorWS {
    private IAdministradorBO adminBO = new AdministradorBOImpl();

    @GET
    @Path("Listar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Administrador> listar() throws Exception {
        return adminBO.listarTodas();
    }

    @GET
    @Path("BuscarPorId")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(@QueryParam("id") int id) {
        try {
            Administrador a = adminBO.buscarPorId(id);
            if (a == null)
                return Response.status(Response.Status.NO_CONTENT).build();
            return Response.ok(a).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("Insertar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertar(Administrador admin, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int id = adminBO.insertar(admin, idUsuarioAccion);
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
    public Response actualizar(Administrador admin, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int r = adminBO.modificar(admin, idUsuarioAccion);
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
            int r = adminBO.eliminar(id, idUsuarioAccion);
            return Response.ok(r).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }
}
