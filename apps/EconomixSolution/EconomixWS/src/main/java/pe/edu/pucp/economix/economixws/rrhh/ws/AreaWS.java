package pe.edu.pucp.economix.economixws.rrhh.ws;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.economix.rrhh.boi.AreaBOImpl;
import pe.edu.pucp.economix.rrhh.ibo.IAreaBO;
import pe.edu.pucp.economix.rrhh.model.Area;

import java.util.List;
import java.util.Map;

@Path("AreaWS")
public class AreaWS {
    private IAreaBO areaBO = new AreaBOImpl();


    @GET
    @Path("ListarAreas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarAreas() {
        try {
            List<Area> areas = areaBO.listarTodas();
            return Response.ok(areas).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("BuscarPorId")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(@QueryParam("id") int id) {
        try {
            Area area = areaBO.buscarPorId(id);
            if (area == null) {
                return Response.status(Response.Status.NO_CONTENT).build();
            }
            return Response.ok(area).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("Insertar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertarArea(Area area, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int id = areaBO.insertar(area, idUsuarioAccion);
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
    public Response actualizarArea(Area area, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int result = areaBO.modificar(area, idUsuarioAccion);
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
    public Response eliminarArea(@QueryParam("id") int id, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int result = areaBO.eliminar(id, idUsuarioAccion);
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
    public List<Area> listarActivas() throws Exception {
        return areaBO.listarActivas();
    }

    @GET
    @Path("Recuperar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response recuperarArea(@QueryParam("id") int id, @QueryParam("idUsuarioAccion") int idUsuarioAccion) {
        try {
            int result = areaBO.recuperar(id, idUsuarioAccion);
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }
}
