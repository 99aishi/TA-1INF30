package pe.edu.pucp.economix.economixws.rrhh.ws;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.economix.rrhh.boi.RolBOImpl;
import pe.edu.pucp.economix.rrhh.ibo.IRolBO;
import pe.edu.pucp.economix.rrhh.model.Rol;

import java.util.List;
import java.util.Map;

@Path("RolWS")
public class RolWS {
    private IRolBO rolBO = new RolBOImpl();

    @GET
    @Path("ListarRoles")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Rol> listarRoles() throws Exception {
        return rolBO.listarTodas();
    }

    @GET
    @Path("BuscarPorId")
    @Produces(MediaType.APPLICATION_JSON)
    public Rol buscarPorId(@QueryParam("id") int id) throws Exception {
        return rolBO.buscarPorId(id);
    }

    @POST
    @Path("Insertar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertarRol(Rol rol) {
        try {
            int id = rolBO.insertar(rol);
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
    public int actualizarRol(Rol rol) throws Exception {
        return rolBO.modificar(rol);
    }

    @GET
    @Path("Eliminar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarRol(@QueryParam("id") int id) {
        try {
            int result = rolBO.eliminar(id);
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }
}
