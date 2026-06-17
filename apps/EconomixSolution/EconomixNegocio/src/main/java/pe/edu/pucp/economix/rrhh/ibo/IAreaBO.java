package pe.edu.pucp.economix.rrhh.ibo;

import java.util.List;

import pe.edu.pucp.economix.ibo.IBaseBO;
import pe.edu.pucp.economix.rrhh.model.Area;

public interface IAreaBO extends IBaseBO<Area> {
    List<Area> listarActivas() throws Exception;
    int recuperar(int id, int idUsuarioAccion) throws Exception;
}
