package pe.edu.pucp.economix.rrhh.idao;

import pe.edu.pucp.economix.idao.IDAO;
import pe.edu.pucp.economix.rrhh.model.Administrador;

import java.sql.SQLException;

public interface IAdministradorDAO extends IDAO<Administrador> {
    int verificarCuenta(String correo, String password) throws SQLException;
}
