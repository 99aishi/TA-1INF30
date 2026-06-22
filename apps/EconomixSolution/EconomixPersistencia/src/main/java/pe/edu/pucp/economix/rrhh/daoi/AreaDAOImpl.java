package pe.edu.pucp.economix.rrhh.daoi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.rrhh.idao.IAreaDAO;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.rrhh.model.RolFlujo;


public class AreaDAOImpl implements  IAreaDAO{
    private ResultSet rs;

    @Override
    public int insertar(Area area, int idUsuarioAccion) throws SQLException {
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_generado", Types.INTEGER);
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_nombre_area", area.getNombre());
        parametrosEntrada.put("p_descripcion_area", area.getDescripcion());
        if(area.getJefe() != null)
            parametrosEntrada.put("p_id_jefe", area.getJefe().getUsuarioID());
        else
            parametrosEntrada.put("p_id_jefe", null);

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_area", parametrosEntrada, parametrosSalida);
        area.setIdArea((int)parametrosSalida.get("p_id_generado"));

        return area.getIdArea();
    }
    @Override
    public int modificar(Area area, int idUsuarioAccion) throws SQLException {
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_area", area.getIdArea());
        parametrosEntrada.put("p_nombre_area", area.getNombre());
        parametrosEntrada.put("p_descripcion_area", area.getDescripcion());
        if(area.getJefe() != null)
            parametrosEntrada.put("p_id_jefe", area.getJefe().getUsuarioID());
        else
            parametrosEntrada.put("p_id_jefe", null);
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_area", parametrosEntrada, null);
        return resultado;
    }
    @Override
    public int asignarJefe(Area area, Empleado empleado, int idUsuarioAccion) throws SQLException {
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_area", area.getIdArea());
        parametrosEntrada.put("p_id_jefe", empleado.getUsuarioID());
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_asignar_jefe_area", parametrosEntrada, null);
        return resultado;
    }
    @Override
    public int eliminar(int idArea, int idUsuarioAccion) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_area", idArea);
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_eliminar_area", parametrosEntrada, null);
        return resultado;
    }
    @Override
    public Area buscarPorId(int idArea) throws SQLException {
        Area area=null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_area", idArea);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_buscar_area_por_id", parametrosEntrada);
        try{
            if(rs.next()){
                area = mapearArea(rs, new HashMap<>());
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar area por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return area;
    }
    @Override
    public List<Area> listarTodas() throws SQLException {
        List<Area> areas = null;
        Area area;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_areas", null);
        try{
            while(rs.next()){
                if(areas == null) areas = new ArrayList<>();
                area = mapearArea(rs, new HashMap<>());
                areas.add(area);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar areas: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return areas;
    }

    @Override
    public List<Area> listarActivas() throws SQLException {
        List<Area> areas = null;
        Area area;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_areas_activas", null);
        try{
            while(rs.next()){
                if(areas == null) areas = new ArrayList<>();
                area = mapearArea(rs, new HashMap<>());
                areas.add(area);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar areas activas: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return areas;
    }

    @SuppressWarnings("unchecked")
    private <T> T getOrCreate(Map<Class<?>, Map<Integer, Object>> cache, Class<T> type, int id, Supplier<T> factory) {
        if (id <= 0) return null;
        return (T) cache.computeIfAbsent(type, k -> new HashMap<>())
                .computeIfAbsent(id, k -> factory.get());
    }

    private Area mapearArea(ResultSet rs, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int idArea = rs.getInt("id_area");
        Area area = getOrCreate(cache, Area.class, idArea, () -> new Area());
        area.setIdArea(idArea);
        area.setNombre(rs.getString("nombre_area"));
        area.setDescripcion(rs.getString("descripcion_area"));
        area.setEstaActivo(rs.getBoolean("esta_activo"));

        Empleado jefe = mapearEmpleadoBasico(rs, "jefe_", cache);
        if (jefe != null) {
            area.setJefe(jefe);
        }

        return area;
    }

    private Empleado mapearEmpleadoBasico(ResultSet rs, String prefijo, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt(prefijo + "id_usuario");
        if (rs.wasNull() || id <= 0) return null;
        return getOrCreate(cache, Empleado.class, id, () -> {
            Empleado e = new Empleado();
            e.setUsuarioID(id);
            try {
                e.setNombres(rs.getString(prefijo + "nombres"));
                e.setApellidoPaterno(rs.getString(prefijo + "apellido_paterno"));
                e.setApellidoMaterno(rs.getString(prefijo + "apellido_materno"));
                e.setCorreo(rs.getString(prefijo + "correo"));
                e.setNumeroCelular(rs.getString(prefijo + "numero_celular"));
                String rolFlujo = rs.getString(prefijo + "rol_flujo");
                if (rolFlujo != null)
                    e.setRolFlujo(RolFlujo.valueOf(rolFlujo));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return e;
        });
    }

    @Override
    public int recuperar(int idArea, int idUsuarioAccion) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_area", idArea);
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_reactivar_area", parametrosEntrada, null);
        return resultado;
    }
}
