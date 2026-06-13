package pe.edu.pucp.economix.rrhh.boi;

import pe.edu.pucp.economix.rrhh.ibo.IUsuarioBO;
import pe.edu.pucp.economix.rrhh.daoi.UsuarioDAOImpl;
import pe.edu.pucp.economix.rrhh.idao.IUsuarioDAO;
import pe.edu.pucp.economix.rrhh.model.Usuario;

public class UsuarioBOImpl implements IUsuarioBO {
    private final IUsuarioDAO usuarioDAO;
    
    public UsuarioBOImpl() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }
    
    @Override
    public Usuario validarUsuario(String correo, String password) throws Exception {
        if (correo == null || correo.trim().isEmpty()) {
            throw new Exception("El correo es obligatorio.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new Exception("La contraseña es obligatoria.");
        }
        
        Usuario usuario = usuarioDAO.buscarPorCorreo(correo);
        if (usuario == null) {
            throw new Exception("Usuario no encontrado.");
        }
        
        if (!usuario.validarPassword(password)) {
            throw new Exception("Contrasena incorrecta.");
        }

        return usuario;
    }
}