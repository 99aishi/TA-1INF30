package pe.edu.pucp.economix.rrhh.daoi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.rrhh.idao.IRolDAO;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Rol;

public class RolDAOImpl implements IRolDAO{
    private ResultSet rs;

    @Override
    public int insertar(Rol rol, int idUsuarioAccion) throws SQLException {
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_generado", Types.INTEGER);
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_titulo_rol", rol.getTitulo());
        parametrosEntrada.put("p_descripcion_rol", rol.getDescripcion());
        parametrosEntrada.put("p_id_area", rol.getArea() != null ? rol.getArea().getIdArea() : null);

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_rol", parametrosEntrada, parametrosSalida);
        rol.setRolID((int)parametrosSalida.get("p_id_generado"));

        return rol.getRolID();
    }
    @Override
    public int modificar(Rol rol, int idUsuarioAccion) throws SQLException {
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_rol", rol.getRolID());
        parametrosEntrada.put("p_titulo_rol", rol.getTitulo());
        parametrosEntrada.put("p_descripcion_rol", rol.getDescripcion());
        parametrosEntrada.put("p_id_area", rol.getArea() != null ? rol.getArea().getIdArea() : null);
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_rol", parametrosEntrada, null);
        return resultado;
    }
    @Override
    public int eliminar(int idRol, int idUsuarioAccion) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_rol", idRol);
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_eliminar_rol", parametrosEntrada, null);
        return resultado;
    }
    @Override
    public Rol buscarPorId(int idRol) throws SQLException {
        Rol rol = null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_rol", idRol);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_buscar_rol_por_id", parametrosEntrada);
        try{
            if(rs.next()){
                rol = mapearRol(rs);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar rol por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return rol;
    }

    @Override
    public List<Rol> listarTodas() throws SQLException{
        List<Rol> roles= null;
        Rol rol;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_roles", null);
        try{
            while(rs.next()){
                if(roles == null) roles = new ArrayList<>();
                rol = mapearRol(rs);
                roles.add(rol);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar roles: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }

        return roles;
    }

    @Override
    public List<Rol> listarPorArea(int idArea) throws SQLException {
        List<Rol> roles = null;
        Rol rol;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_area", idArea);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_roles_por_area", parametrosEntrada);
        try{
            while(rs.next()){
                if(roles == null) roles = new ArrayList<>();
                rol = mapearRol(rs);
                roles.add(rol);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar roles por area: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }

        return roles;
    }

    private Rol mapearRol(ResultSet rs) throws SQLException {
        Rol rol = new Rol();
        rol.setRolID(rs.getInt("id_rol"));
        rol.setTitulo(rs.getString("titulo_rol"));
        rol.setDescripcion(rs.getString("descripcion_rol"));
        rol.setEstaActivo(rs.getBoolean("esta_activo"));

        int idArea = rs.getInt("id_area");
        if (!rs.wasNull()) {
            Area area = new Area();
            area.setIdArea(idArea);
            rol.setArea(area);
        }

        return rol;
    }
}


