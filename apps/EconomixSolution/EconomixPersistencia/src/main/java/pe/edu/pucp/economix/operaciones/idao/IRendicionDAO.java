package pe.edu.pucp.economix.operaciones.idao;

import pe.edu.pucp.economix.operaciones.model.Rendicion;

import java.sql.SQLException;

public interface IRendicionDAO {
    int insertar(Rendicion rendicion, int idUsuarioAccion) throws SQLException;
    int modificar(Rendicion rendicion, int idUsuarioAccion) throws SQLException;
    int eliminar(int id, int idUsuarioAccion) throws SQLException;
    Rendicion buscarPorId(int id) throws SQLException;
    java.util.List<Rendicion> listarActivas() throws SQLException;
    java.util.List<Rendicion> listarTodas() throws SQLException;
}
