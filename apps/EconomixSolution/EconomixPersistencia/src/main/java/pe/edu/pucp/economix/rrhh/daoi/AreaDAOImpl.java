package pe.edu.pucp.economix.rrhh.daoi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.rrhh.idao.IAreaDAO;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;

public class AreaDAOImpl implements  IAreaDAO{
    private ResultSet rs;

    @Override
    public int insertar(Area area) throws SQLException {
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_generado", Types.INTEGER);
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
    public int modificar(Area area) throws SQLException {
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_area", area.getIdArea());
        parametrosEntrada.put("p_nombre_are", area.getNombre());
        parametrosEntrada.put("p_descripcion_area", area.getDescripcion());
        parametrosEntrada.put("p_id_jefe", area.getJefe().getUsuarioID());
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_area", parametrosEntrada, null);
        return resultado;
    }
    @Override
    public int asignarJefe(Area area, Empleado empleado) throws SQLException {
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_area", area.getIdArea());
        parametrosEntrada.put("p_id_jefe", empleado.getUsuarioID());
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_asignar_jefe_area", parametrosEntrada, null);
        return resultado;
    }
    @Override
    public int eliminar(int idArea) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario", idArea);
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
                area = new Area();
                area.setIdArea(rs.getInt("id_area"));
                area.setNombre(rs.getString("nombre_area"));
                area.setDescripcion(rs.getString("descripcion_area"));
                if(area.getJefe() == null)
                    area.setJefe(new Empleado());
                area.getJefe().setUsuarioID(rs.getInt("id_jefe"));
                int idFondo = rs.getInt("id_fondo_caja_chica");
                if (!rs.wasNull()) {
                    CajaChica cc = new CajaChica();
                    cc.setIdFondo(idFondo);
                    area.setCajaChica(cc);
                }
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
                area = new Area();
                area.setIdArea(rs.getInt("id_area"));
                area.setNombre(rs.getString("nombre_area"));
                area.setDescripcion(rs.getString("descripcion_area"));
                if(area.getJefe() == null)
                    area.setJefe(new Empleado());
                area.getJefe().setUsuarioID(rs.getInt("id_jefe"));
                int idFondo = rs.getInt("id_fondo_caja_chica");
                if (!rs.wasNull()) {
                    CajaChica cc = new CajaChica();
                    cc.setIdFondo(idFondo);
                    area.setCajaChica(cc);
                }
                areas.add(area);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar areas: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return areas;
    }
}