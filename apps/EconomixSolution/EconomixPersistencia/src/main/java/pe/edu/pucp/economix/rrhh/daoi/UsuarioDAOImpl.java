package pe.edu.pucp.economix.rrhh.daoi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.rrhh.idao.IUsuarioDAO;
import pe.edu.pucp.economix.rrhh.model.Administrador;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.rrhh.model.EstadoUsuario;
import pe.edu.pucp.economix.rrhh.model.Rol;
import pe.edu.pucp.economix.rrhh.model.RolFlujo;
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
        empleado.setNumeroCelular(rs.getString("numero_celular"));
        
        String estadoStr = rs.getString("estado");
        empleado.setEstado("ACTIVO".equals(estadoStr) ? EstadoUsuario.ACTIVO : EstadoUsuario.INACTIVO);
        
        String rolFlujoStr = rs.getString("rol_flujo");
        if (rolFlujoStr != null) {
            empleado.setRolFlujo(RolFlujo.valueOf(rolFlujoStr));
        }
        
        int idArea = rs.getInt("id_area");
        if (!rs.wasNull()) {
            Area area = new Area();
            area.setIdArea(idArea);
            area.setNombre(rs.getString("nombre_area"));
            area.setDescripcion(rs.getString("area_descripcion"));
            area.setEstaActivo(rs.getBoolean("area_esta_activo"));

            int idAreaJefe = rs.getInt("area_id_jefe");
            if (!rs.wasNull()) {
                Empleado areaJefe = new Empleado();
                areaJefe.setUsuarioID(idAreaJefe);
                areaJefe.setNombres(rs.getString("area_jefe_nombres"));
                areaJefe.setApellidoPaterno(rs.getString("area_jefe_apellido_paterno"));
                areaJefe.setApellidoMaterno(rs.getString("area_jefe_apellido_materno"));
                areaJefe.setCorreo(rs.getString("area_jefe_correo"));
                areaJefe.setNumeroCelular(rs.getString("area_jefe_numero_celular"));
                String areaJefeRolFlujoStr = rs.getString("area_jefe_rol_flujo");
                if (areaJefeRolFlujoStr != null) {
                    areaJefe.setRolFlujo(RolFlujo.valueOf(areaJefeRolFlujoStr));
                }
                area.setJefe(areaJefe);
            }

            empleado.setArea(area);
        }
        
        int idRol = rs.getInt("id_rol");
        if (!rs.wasNull()) {
            Rol rol = new Rol();
            rol.setRolID(idRol);
            rol.setTitulo(rs.getString("titulo_rol"));
            rol.setDescripcion(rs.getString("rol_descripcion"));
            rol.setEstaActivo(rs.getBoolean("rol_esta_activo"));
            empleado.setRol(rol);
        }
        
        int idJefeDirecto = rs.getInt("id_jefe_directo");
        if (!rs.wasNull()) {
            Empleado jefeDirecto = new Empleado();
            jefeDirecto.setUsuarioID(idJefeDirecto);
            jefeDirecto.setNombres(rs.getString("jefe_nombres"));
            jefeDirecto.setApellidoPaterno(rs.getString("jefe_apellido_paterno"));
            jefeDirecto.setApellidoMaterno(rs.getString("jefe_apellido_materno"));
            jefeDirecto.setCorreo(rs.getString("jefe_correo"));
            jefeDirecto.setNumeroCelular(rs.getString("jefe_numero_celular"));
            String jefeRolFlujoStr = rs.getString("jefe_rol_flujo");
            if (jefeRolFlujoStr != null) {
                jefeDirecto.setRolFlujo(RolFlujo.valueOf(jefeRolFlujoStr));
            }
            empleado.setJefeDirecto(jefeDirecto);
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