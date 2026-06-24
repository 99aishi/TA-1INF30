package pe.edu.pucp.economix.economixws.operaciones.ws;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.economix.operaciones.boi.RendicionBOImpl;
import pe.edu.pucp.economix.operaciones.ibo.IRendicionBO;
import pe.edu.pucp.economix.operaciones.model.Rendicion;

import java.util.List;
import java.util.Map;

@Path("RendicionWS")
public class RendicionWS {
    private IRendicionBO rendicionBO = new RendicionBOImpl();

    @GET
    @Path("Listar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Rendicion> listar() throws Exception {
        return rendicionBO.listarTodas();
    }

    @GET
    @Path("BuscarPorId")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(@QueryParam("id") int id) {
        try {
            Rendicion r = rendicionBO.buscarPorId(id);
            if (r == null)
                return Response.status(Response.Status.NO_CONTENT).build();
            return Response.ok(r).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("Insertar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertar(Rendicion r, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int id = rendicionBO.insertar(r, idUsuarioAccion);
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
    public Response actualizar(Rendicion r, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int res = rendicionBO.modificar(r, idUsuarioAccion);
            return Response.ok(res).build();
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
            int res = rendicionBO.eliminar(id, idUsuarioAccion);
            return Response.ok(res).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Path("GenerarRendicionDeCiclo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response generarRendicionDeCiclo(@QueryParam("idCiclo") int idCiclo, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            Rendicion r = rendicionBO.generarRendicionDeCiclo(idCiclo, idUsuarioAccion);
            return Response.ok(r).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("CalcularTotales")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response calcularTotales(Rendicion r, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            rendicionBO.calcularTotales(r, idUsuarioAccion);
            return Response.ok(r).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("ObservarRendicion")
    @Produces(MediaType.APPLICATION_JSON)
    public Response observarRendicion(@QueryParam("idRendicion") int idRendicion,
                                      @QueryParam("comentario") String comentario,
                                      @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            rendicionBO.observarRendicion(idRendicion, comentario, idUsuarioAccion);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("AceptarRendicion")
    @Produces(MediaType.APPLICATION_JSON)
    public Response aceptarRendicion(@QueryParam("idRendicion") int idRendicion,
                                     @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            rendicionBO.aceptarRendicion(idRendicion, idUsuarioAccion);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("DenegarRendicion")
    @Produces(MediaType.APPLICATION_JSON)
    public Response denegarRendicion(@QueryParam("idRendicion") int idRendicion,
                                     @QueryParam("comentario") String comentario,
                                     @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            rendicionBO.denegarRendicion(idRendicion, comentario, idUsuarioAccion);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("ReEnviarRendicion")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reEnviarRendicion(@QueryParam("idRendicion") int idRendicion,
                                      @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            rendicionBO.reEnviarRendicion(idRendicion, idUsuarioAccion);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Path("ListarPorArea")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Rendicion> listarPorArea(@QueryParam("idArea") int idArea) throws Exception {
        return rendicionBO.listarPorArea(idArea);
    }
}
