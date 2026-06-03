package pe.edu.pucp.economix.rrhh.ibo;

import pe.edu.pucp.economix.ibo.IBaseBO;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.rrhh.model.Empleado;

import java.sql.SQLException;
import java.util.List;

public interface IEmpleadoBO extends IBaseBO<Empleado> {
    int verificarCuenta(String correo, String password) throws Exception;
    List<Empleado> listarPorNombreApellido(String busqueda) throws Exception;
}
