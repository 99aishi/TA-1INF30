package pe.edu.pucp.economix.rrhh.boi;

import java.util.Map;

import pe.edu.pucp.economix.auditoria.idao.IAuditoriaDAO;
import pe.edu.pucp.economix.auditoria.daoi.AuditoriaDAOImpl;
import pe.edu.pucp.economix.rrhh.ibo.IUsuarioBO;
import pe.edu.pucp.economix.rrhh.daoi.UsuarioDAOImpl;
import pe.edu.pucp.economix.rrhh.idao.IUsuarioDAO;
import pe.edu.pucp.economix.rrhh.model.EstadoUsuario;
import pe.edu.pucp.economix.rrhh.model.Usuario;

public class UsuarioBOImpl implements IUsuarioBO {
    private final IUsuarioDAO usuarioDAO;
    private final IAuditoriaDAO auditoriaDAO;

    public UsuarioBOImpl() {
        this.usuarioDAO = new UsuarioDAOImpl();
        this.auditoriaDAO = new AuditoriaDAOImpl();
    }

    @Override
    public Usuario validarUsuario(String correo, String password) throws Exception {
        if (correo == null || correo.trim().isEmpty()) {
            throw new Exception("El correo es obligatorio.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new Exception("La contraseña es obligatoria.");
        }

        correo = correo.trim();

        // Verificar si el usuario esta bloqueado por intentos fallidos
        Map<String, Object> estadoBloqueo = usuarioDAO.verificarBloqueo(correo);
        boolean bloqueado = (Boolean) estadoBloqueo.get("bloqueado");
        Integer minutosRestantes = (Integer) estadoBloqueo.get("minutosRestantes");

        if (bloqueado) {
            auditoriaDAO.registrarLogin(correo, (Integer) estadoBloqueo.get("idUsuario"), false, 0, true);
            throw new Exception("Cuenta bloqueada por intentos fallidos. Intente nuevamente en "
                + minutosRestantes + " minutos.");
        }

        Usuario usuario = usuarioDAO.buscarPorCorreo(correo);
        if (usuario == null) {
            // Correo no existe: registrar intento fallido anonimo
            usuarioDAO.registrarIntentoFallido(correo, null);
            Map<String, Object> estadoDespues = usuarioDAO.verificarBloqueo(correo);
            int intentosRestantes = (Integer) estadoDespues.get("intentosRestantes");
            boolean ahoraBloqueado = (Boolean) estadoDespues.get("bloqueado");
            auditoriaDAO.registrarLogin(correo, null, false, 5 - intentosRestantes, ahoraBloqueado);

            if (ahoraBloqueado) {
                throw new Exception("Cuenta bloqueada por intentos fallidos. Intente nuevamente en "
                    + estadoDespues.get("minutosRestantes") + " minutos.");
            }
            throw new Exception("Usuario o contraseña incorrectos. Intentos restantes: " + intentosRestantes);
        }

        // Verificar si el usuario esta inactivo o eliminado
        if (usuario.getEstado() == EstadoUsuario.INACTIVO) {
            auditoriaDAO.registrarLogin(correo, usuario.getUsuarioID(), false, 0, false);
            throw new Exception("Usuario inactivo o eliminado.");
        }

        if (!usuario.validarPassword(password)) {
            // Contrasena incorrecta
            usuarioDAO.registrarIntentoFallido(correo, usuario.getUsuarioID());
            Map<String, Object> estadoDespues = usuarioDAO.verificarBloqueo(correo);
            int intentosRestantes = (Integer) estadoDespues.get("intentosRestantes");
            boolean ahoraBloqueado = (Boolean) estadoDespues.get("bloqueado");
            int intentosFallidos = 5 - intentosRestantes;
            auditoriaDAO.registrarLogin(correo, usuario.getUsuarioID(), false, intentosFallidos, ahoraBloqueado);

            if (ahoraBloqueado) {
                throw new Exception("Cuenta bloqueada por intentos fallidos. Intente nuevamente en "
                    + estadoDespues.get("minutosRestantes") + " minutos.");
            }
            throw new Exception("Usuario o contraseña incorrectos. Intentos restantes: " + intentosRestantes);
        }

        // Login exitoso: resetear intentos y registrar auditoria
        usuarioDAO.resetearIntentos(correo);
        auditoriaDAO.registrarLogin(correo, usuario.getUsuarioID(), true, 0, false);

        return usuario;
    }

    @Override
    public int registrarIntentoFallido(String correo, Integer idUsuario) throws Exception {
        usuarioDAO.registrarIntentoFallido(correo, idUsuario);
        Map<String, Object> estado = usuarioDAO.verificarBloqueo(correo);
        return (Integer) estado.get("intentosRestantes");
    }

    @Override
    public void resetearIntentos(String correo) throws Exception {
        usuarioDAO.resetearIntentos(correo);
    }
}
