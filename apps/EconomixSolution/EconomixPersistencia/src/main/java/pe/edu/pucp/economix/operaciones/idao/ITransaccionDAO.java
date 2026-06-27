package pe.edu.pucp.economix.operaciones.idao;

import pe.edu.pucp.economix.operaciones.model.Transaccion;

import java.sql.SQLException;

public interface ITransaccionDAO {
    int insertar(Transaccion transaccion, int idUsuarioAccion) throws SQLException;
    int modificar(Transaccion transaccion, int idUsuarioAccion) throws SQLException;
    int eliminar(int id, int idUsuarioAccion) throws SQLException;
    Transaccion buscarPorId(int id) throws SQLException;
    java.util.List<Transaccion> listarTodas() throws SQLException;
    java.util.List<Transaccion> listarActivas() throws SQLException;
    java.util.List<Transaccion> listarPorJefe(int idJefe) throws SQLException;
}
