package pe.edu.pucp.economix.rrhh.idao;

import pe.edu.pucp.economix.rrhh.model.Empleado;

import java.sql.SQLException;
import java.util.List;

public interface IEmpleadoDAO {
    int insertar(Empleado empleado, int idUsuarioAccion) throws SQLException;
    int modificar(Empleado empleado, int idUsuarioAccion) throws SQLException;
    int eliminar(int id, int idUsuarioAccion) throws SQLException;
    Empleado buscarPorId(int id) throws SQLException;
    List<Empleado> listarActivas() throws SQLException;
    List<Empleado> listarTodas() throws SQLException;
    int verificarCuenta(String correo, String password) throws SQLException;
    List<Empleado> listarPorNombreApellido(String busqueda) throws SQLException;
}
