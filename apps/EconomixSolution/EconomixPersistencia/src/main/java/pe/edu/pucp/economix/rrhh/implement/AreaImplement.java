package pe.edu.pucp.economix.rrhh.implement;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.rrhh.dao.IAreaDAO;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AreaImplement implements  IAreaDAO{
    private Connection con;
    private ResultSet rs;
    private CallableStatement cs;

    @Override
    public int insertar(Area area){
        int id=0;
        try{
            con = DBManager.getDBManager().getConnection();
            cs= con.prepareCall("{call pa_insertar_area(?,?,?,?)}");
            cs.setString("p_nombre_area", area.getNombre());
            cs.setString("p_descripcion_area", area.getDescripcion());
            if(area.getJefe() != null)
                cs.setInt("p_id_jefe", area.getJefe().getUsuarioID());
            else
                cs.setNull("p_id_jefe", java.sql.Types.INTEGER);
            cs.registerOutParameter("p_id_generado", java.sql.Types.INTEGER); // TODO Revisión INTEGER

            cs.executeUpdate();

            id = cs.getInt("p_id_generado");

        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }finally {
            try{
                cs.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
            try {
                con.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
        }

        return id;
    }
    @Override
    public int modificar(Area area){
        try{
            con = DBManager.getDBManager().getConnection();
            cs= con.prepareCall("{call pa_modificar_area(?,?,?,?)}");
            cs.setInt("p_id_area", area.getIdArea());
            cs.setString("p_nombre_area", area.getNombre());
            cs.setString("p_descripcion_area", area.getDescripcion());
            cs.setInt("p_id_jefe", area.getJefe().getUsuarioID());

            return cs.executeUpdate();

        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }finally {
            try{
                cs.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
            try {
                con.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
        }
        int cantidad=0;
        return cantidad;
    }
    @Override
    public int asignarJefe(Area area, Empleado empleado){
        try{
            con = DBManager.getDBManager().getConnection();
            cs= con.prepareCall("{call pa_asignar_jefe_area(?,?)}");

            cs.setInt("p_id_area", area.getIdArea());
            cs.setInt("p_id_jefe", empleado.getUsuarioID());

            return cs.executeUpdate();
        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }finally {
            try{
                cs.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
            try {
                con.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
        }
        int cantidad=0;
        return cantidad;
    }
    @Override
    public int eliminar(int idArea){
        int cantidad=0;
        return cantidad;
    }
    @Override
    public Area buscarPorId(int idArea){
        Area area=null;
        try{
            con = DBManager.getDBManager().getConnection();
            cs= con.prepareCall("{call pa_buscar_area_por_id(?)}");

            cs.setInt("p_id_area", idArea);

            rs = cs.executeQuery();
            if(rs.next()){
                Empleado jefe = new Empleado();
                area = new Area();
                area.setIdArea(rs.getInt("id_area"));
                area.setNombre(rs.getString("nombre_area"));
                area.setDescripcion(rs.getString("descripcion_area"));
                jefe.setUsuarioID(rs.getInt("id_jefe"));
                area.setJefe(jefe);
            }
            return area;
        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }finally {
            try{
                cs.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
            try {
                con.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
        }

        return null;
    }
    @Override
    public List<Area> listarTodas() {
        List<Area> areas = null;
        try{
            con = DBManager.getDBManager().getConnection();
            cs= con.prepareCall("{call pa_listar_areas()}");

            rs = cs.executeQuery();

            while(rs.next()){
                if(areas == null)
                    areas = new ArrayList<Area>();
                Area area = new Area();
                area.setIdArea(rs.getInt("id_area"));
                area.setNombre(rs.getString("nombre_area"));
                area.setDescripcion(rs.getString("descripcion_area"));

                areas.add(area);
            }
            return areas;
        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }finally {
            try{
                cs.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
            try {
                con.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
        }
        return areas;
    }
}