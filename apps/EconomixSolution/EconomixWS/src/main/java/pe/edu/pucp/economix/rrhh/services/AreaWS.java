package pe.edu.pucp.economix.rrhh.services;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import pe.edu.pucp.economix.rrhh.boi.AreaBOImpl;
import pe.edu.pucp.economix.rrhh.ibo.IAreaBO;
import pe.edu.pucp.economix.rrhh.model.Area;

import java.util.List;


@WebService(
        serviceName = "AreaWS",
        targetNamespace = "http://services.economix.pucp.edu.pe"
)

public class AreaWS {
    private IAreaBO areaBO = new AreaBOImpl();

    @WebMethod(
        operationName = "lsitarAreas"
    )
    public List<Area> listarAreas() throws Exception {
        return areaBO.listarTodas();
    }
}
