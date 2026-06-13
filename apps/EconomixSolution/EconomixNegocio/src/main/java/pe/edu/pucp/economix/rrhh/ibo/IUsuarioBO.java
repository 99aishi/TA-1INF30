package pe.edu.pucp.economix.rrhh.ibo;

import pe.edu.pucp.economix.rrhh.model.Usuario;

public interface IUsuarioBO {
    Usuario validarUsuario(String correo, String password) throws Exception;
}