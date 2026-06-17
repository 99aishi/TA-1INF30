package pe.edu.pucp.economix.economixws.rrhh.ws;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.economix.rrhh.boi.EmpleadoBOImpl;
import pe.edu.pucp.economix.rrhh.ibo.IEmpleadoBO;
import pe.edu.pucp.economix.rrhh.model.Empleado;

import java.util.List;
import java.util.Map;

@Path("EmpleadoWS")
public class EmpleadoWS {
    private IEmpleadoBO empleadoBO = new EmpleadoBOImpl();

    @GET
    @Path("ListarEmpleados")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Empleado> listarEmpleados() throws Exception {
        return empleadoBO.listarTodas();
    }

    @GET
    @Path("BuscarPorId")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(@QueryParam("id") int id) {
        try {
            Empleado emp = empleadoBO.buscarPorId(id);
            if (emp == null)
                return Response.status(Response.Status.NO_CONTENT).build();
            return Response.ok(emp).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Path("ListarPorNombreApellido")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Empleado> listarPorNombreApellido(@QueryParam("q") String q) throws Exception {
        return empleadoBO.listarPorNombreApellido(q);
    }

    @POST
    @Path("ValidarCredenciales")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validarCredenciales(Map<String, String> credenciales) {
        try {
            String correo = credenciales.get("correo");
            String password = credenciales.get("password");
            int id = empleadoBO.verificarCuenta(correo, password);
            return id > 0 ? Response.ok(id).build()
                    : Response.status(Response.Status.UNAUTHORIZED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("Insertar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertar(Empleado empleado, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int id = empleadoBO.insertar(empleado, idUsuarioAccion);
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
    public Response actualizar(Empleado empleado, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int r = empleadoBO.modificar(empleado, idUsuarioAccion);
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
            int r = empleadoBO.eliminar(id, idUsuarioAccion);
            return Response.ok(r).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }
}
