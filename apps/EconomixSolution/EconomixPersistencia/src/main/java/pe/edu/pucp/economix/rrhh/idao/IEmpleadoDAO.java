package pe.edu.pucp.economix.rrhh.idao;

import pe.edu.pucp.economix.idao.IDAO;
import pe.edu.pucp.economix.rrhh.model.Empleado;

import java.sql.SQLException;
import java.util.List;

public interface IEmpleadoDAO extends IDAO<Empleado> {
    int verificarCuenta(String correo, String password) throws SQLException;
    List<Empleado> listarPorNombreApellido(String busqueda) throws SQLException;
}
