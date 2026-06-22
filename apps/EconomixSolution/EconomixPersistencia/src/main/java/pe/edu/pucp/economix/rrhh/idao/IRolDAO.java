package pe.edu.pucp.economix.rrhh.idao;

import pe.edu.pucp.economix.rrhh.model.Rol;

import java.sql.SQLException;

public interface IRolDAO {
    int insertar(Rol rol, int idUsuarioAccion) throws SQLException;
    int modificar(Rol rol, int idUsuarioAccion) throws SQLException;
    int eliminar(int id, int idUsuarioAccion) throws SQLException;
    Rol buscarPorId(int id) throws SQLException;
    java.util.List<Rol> listarTodas() throws SQLException;
    java.util.List<Rol> listarPorArea(int idArea) throws SQLException;
}
