package pe.edu.pucp.economix.rrhh.ibo;

import pe.edu.pucp.economix.rrhh.model.Usuario;

public interface IUsuarioBO {
    Usuario validarUsuario(String correo, String password) throws Exception;

    /**
     * Registra un intento de login fallido y devuelve los intentos restantes.
     */
    int registrarIntentoFallido(String correo, Integer idUsuario) throws Exception;

    void resetearIntentos(String correo) throws Exception;
}
