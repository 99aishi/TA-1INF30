package pe.edu.pucp.economix.rrhh.daoi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.rrhh.idao.IUsuarioDAO;
import pe.edu.pucp.economix.rrhh.model.Administrador;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.rrhh.model.EstadoUsuario;
import pe.edu.pucp.economix.rrhh.model.Rol;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Usuario;

public class UsuarioDAOImpl implements IUsuarioDAO {
    private ResultSet rs;

    @Override
    public Usuario buscarPorCorreo(String correo) throws SQLException {
        Usuario usuario = null;
        Map<String, Object> parametrosEntrada = Map.of("p_correo", correo);
        
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura(
            "pa_buscar_usuario_por_correo", parametrosEntrada);
        
        try {
            if (rs.next()) {
                String tipoUsuario = rs.getString("tipo_usuario");
                
                if ("EMPLEADO".equals(tipoUsuario)) {
                    usuario = new Empleado(mapearEmpleado(rs));
                } else if ("ADMINISTRADOR".equals(tipoUsuario)) {
                    usuario = new Administrador(mapearAdministrador(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar usuario por correo: " + ex.getMessage());
            throw ex;
        } finally {
            DBManager.getDBManager().cerrarConexion();
        }
        
        return usuario;
    }
    
    private Empleado mapearEmpleado(ResultSet rs) throws SQLException {
        Empleado empleado = new Empleado();
        empleado.setUsuarioID(rs.getInt("id_usuario"));
        empleado.setNombres(rs.getString("nombres"));
        empleado.setApellidoPaterno(rs.getString("apellido_paterno"));
        empleado.setApellidoMaterno(rs.getString("apellido_materno"));
        empleado.setPasswordHash(rs.getString("password_hash"));
        empleado.setCorreo(rs.getString("correo"));
        
        String estadoStr = rs.getString("estado");
        empleado.setEstado("ACTIVO".equals(estadoStr) ? EstadoUsuario.ACTIVO : EstadoUsuario.INACTIVO);
        
        int idArea = rs.getInt("id_area");
        if (!rs.wasNull()) {
            Area area = new Area();
            area.setIdArea(idArea);
            area.setNombre(rs.getString("nombre_area"));
            empleado.setArea(area);
        }
        
        int idRol = rs.getInt("id_rol");
        if (!rs.wasNull()) {
            Rol rol = new Rol();
            rol.setRolID(idRol);
            rol.setTitulo(rs.getString("titulo_rol"));
            empleado.setRol(rol);
        }
        
        return empleado;
    }
    
    private Administrador mapearAdministrador(ResultSet rs) throws SQLException {
        Administrador admin = new Administrador();
        admin.setUsuarioID(rs.getInt("id_usuario"));
        admin.setNombres(rs.getString("nombres"));
        admin.setApellidoPaterno(rs.getString("apellido_paterno"));
        admin.setApellidoMaterno(rs.getString("apellido_materno"));
        admin.setPasswordHash(rs.getString("password_hash"));
        admin.setCorreo(rs.getString("correo"));
        
        String estadoStr = rs.getString("estado");
        admin.setEstado("ACTIVO".equals(estadoStr) ? EstadoUsuario.ACTIVO : EstadoUsuario.INACTIVO);
        
        return admin;
    }
}