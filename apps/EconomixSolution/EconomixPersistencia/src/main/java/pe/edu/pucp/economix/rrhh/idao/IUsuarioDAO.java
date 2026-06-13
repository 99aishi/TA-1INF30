package pe.edu.pucp.economix.rrhh.idao;

import java.sql.SQLException;
import pe.edu.pucp.economix.rrhh.model.Usuario;

public interface IUsuarioDAO {
    Usuario buscarPorCorreo(String correo) throws SQLException;
}