package pe.edu.pucp.economix.economixws.tesoreria.ws;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.economix.tesoreria.boi.CuentaBancariaBOImpl;
import pe.edu.pucp.economix.tesoreria.ibo.ICuentaBancariaBO;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;

import java.util.List;
import java.util.Map;

@Path("CuentaBancariaWS")
public class CuentaBancariaWS {
    private ICuentaBancariaBO cuentaBO = new CuentaBancariaBOImpl();

    @GET
    @Path("Listar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CuentaBancaria> listar() throws Exception {
        return cuentaBO.listarTodas();
    }

    @GET
    @Path("BuscarPorId")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(@QueryParam("id") int id) {
        try {
            CuentaBancaria cb = cuentaBO.buscarPorId(id);
            if (cb == null)
                return Response.status(Response.Status.NO_CONTENT).build();
            return Response.ok(cb).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("Insertar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertar(CuentaBancaria cuenta, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int id = cuentaBO.insertar(cuenta, idUsuarioAccion);
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
    public Response actualizar(CuentaBancaria cuenta, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int r = cuentaBO.modificar(cuenta, idUsuarioAccion);
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
            int r = cuentaBO.eliminar(id, idUsuarioAccion);
            return Response.ok(r).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Path("ListarPorEmpleado")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarPorEmpleado(@QueryParam("id") int id) {
        try {
            List<CuentaBancaria> cuentas = cuentaBO.listarPorEmpleado(id);
            if (cuentas == null || cuentas.isEmpty())
                return Response.status(Response.Status.NO_CONTENT).build();
            return Response.ok(cuentas).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Path("ListarCajasChicas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarCajasChicas(@QueryParam("id") int id) {
        try {
            List<CajaChica> cajas = cuentaBO.listarCajasChicas(id);
            if (cajas == null || cajas.isEmpty())
                return Response.status(Response.Status.NO_CONTENT).build();
            return Response.ok(cajas).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }
}
