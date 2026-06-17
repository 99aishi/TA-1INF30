package pe.edu.pucp.economix.rrhh.idao;

import pe.edu.pucp.economix.rrhh.model.Administrador;

import java.sql.SQLException;

public interface IAdministradorDAO {
    int insertar(Administrador administrador, int idUsuarioAccion) throws SQLException;
    int modificar(Administrador administrador, int idUsuarioAccion) throws SQLException;
    int eliminar(int id, int idUsuarioAccion) throws SQLException;
    Administrador buscarPorId(int id) throws SQLException;
    java.util.List<Administrador> listarActivas() throws SQLException;
    java.util.List<Administrador> listarTodas() throws SQLException;
    int verificarCuenta(String correo, String password) throws SQLException;
}
