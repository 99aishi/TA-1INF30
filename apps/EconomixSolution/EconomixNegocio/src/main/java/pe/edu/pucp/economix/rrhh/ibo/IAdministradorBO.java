package pe.edu.pucp.economix.rrhh.ibo;

import pe.edu.pucp.economix.ibo.IBaseBO;
import pe.edu.pucp.economix.rrhh.model.Administrador;

import java.sql.SQLException;
import java.util.List;

public interface IAdministradorBO extends IBaseBO<Administrador> {
    int verificarCuenta(String correo, String password) throws Exception;
    List<Administrador> listarActivas() throws Exception;
}
