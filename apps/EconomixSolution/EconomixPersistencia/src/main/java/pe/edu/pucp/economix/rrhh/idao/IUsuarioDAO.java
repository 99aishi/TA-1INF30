package pe.edu.pucp.economix.rrhh.idao;

import java.sql.SQLException;
import java.util.Map;
import pe.edu.pucp.economix.rrhh.model.Usuario;

public interface IUsuarioDAO {
    Usuario buscarPorCorreo(String correo) throws SQLException;

    /**
     * Verifica el estado de bloqueo de un usuario por correo.
     * @return Mapa con: bloqueado (Boolean), intentosRestantes (Integer),
     *         minutosRestantes (Integer), idUsuario (Integer)
     */
    Map<String, Object> verificarBloqueo(String correo) throws SQLException;

    void registrarIntentoFallido(String correo, Integer idUsuario) throws SQLException;

    void resetearIntentos(String correo) throws SQLException;
}
