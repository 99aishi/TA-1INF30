package pe.edu.pucp.economix.economixws.rrhh.ws;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.edu.pucp.economix.rrhh.boi.AreaBOImpl;
import pe.edu.pucp.economix.rrhh.ibo.IAreaBO;
import pe.edu.pucp.economix.rrhh.model.Area;

import java.util.List;

@Path("AreaWS")
public class AreaWS {
    private IAreaBO areaBO = new AreaBOImpl();


    @GET
    @Path("ListarAreas")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Area> listarAreas() throws Exception {
        return areaBO.listarTodas();
    }

    @POST
    @Path("Insertar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public int insertarArea(Area area) throws Exception {
        return areaBO.insertar(area);
    }
}
