package pe.edu.pucp.economix.rrhh.ibo;

import java.util.List;

import pe.edu.pucp.economix.ibo.IBaseBO;
import pe.edu.pucp.economix.rrhh.model.Rol;

public interface IRolBO extends IBaseBO<Rol> {
    List<Rol> listarPorArea(int idArea) throws Exception;
}
