package pe.edu.pucp.economix.economixws.rrhh.ws;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import pe.edu.pucp.economix.rrhh.boi.AreaBOImpl;
import pe.edu.pucp.economix.rrhh.ibo.IAreaBO;
import pe.edu.pucp.economix.rrhh.model.Area;

import java.util.List;

@Path("AreaRS")
public class AreaWS {
    private IAreaBO areaBO = new AreaBOImpl();
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Area> listarAreas() throws Exception {
        return areaBO.listarTodas();
    }
}
