package pe.edu.pucp.economix.operaciones.idao;

import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;

import java.sql.SQLException;

public interface ICicloCajaChicaDAO {
    int insertar(CicloCajaChica cicloCajaChica, int idUsuarioAccion) throws SQLException;
    int insertarEnTransaccion(CicloCajaChica cicloCajaChica, int idUsuarioAccion) throws SQLException;
    int modificar(CicloCajaChica cicloCajaChica, int idUsuarioAccion) throws SQLException;
    int eliminar(int id, int idUsuarioAccion) throws SQLException;
    CicloCajaChica buscarPorId(int id) throws SQLException;
    java.util.List<CicloCajaChica> listarActivos() throws SQLException;
    java.util.List<CicloCajaChica> listarPasados() throws SQLException;
    java.util.List<CicloCajaChica> listarTodas() throws SQLException;
}
