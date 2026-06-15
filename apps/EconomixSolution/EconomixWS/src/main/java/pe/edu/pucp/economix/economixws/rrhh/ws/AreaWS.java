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
    public List<Area> listarAreas() throws Exception {
        return areaBO.listarTodas();
    }

    @GET
    @Path("BuscarPorId")
    @Produces(MediaType.APPLICATION_JSON)
    public Area buscarPorId(@QueryParam("id") int id) throws Exception {
        return areaBO.buscarPorId(id);
    }

    @POST
    @Path("Insertar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertarArea(Area area) {
        try {
            int id = areaBO.insertar(area);
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
    public int actualizarArea(Area area) throws Exception {
        return areaBO.modificar(area);
    }

    @POST
    @Path("Eliminar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public int eliminarArea(Area area) throws Exception {
        return areaBO.eliminar(area.getIdArea());
    }
}
