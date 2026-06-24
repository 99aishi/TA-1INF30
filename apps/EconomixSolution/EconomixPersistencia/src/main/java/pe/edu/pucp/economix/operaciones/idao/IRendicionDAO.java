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
    int cambiarEstadoRendicion(int idRendicion, String nuevoEstado, String comentario, int idUsuarioAccion) throws SQLException;
    java.util.List<Rendicion> listarPorArea(int idArea) throws SQLException;
    int generarRendicionDeCicloSP(int idCiclo, int idUsuarioAccion) throws SQLException;
}
