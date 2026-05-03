package pe.edu.pucp.economix.rrhh.implement;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.rrhh.dao.IRolDAO;
import pe.edu.pucp.economix.rrhh.model.Rol;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RolImplement  implements IRolDAO{
    private Connection con;
    private ResultSet rs;
    private CallableStatement cs;

    @Override
    public int insertar(Rol rol) throws SQLException {
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_generado", Types.INTEGER);
        parametrosEntrada.put("p_titulo_rol", rol.getTitulo());
        parametrosEntrada.put("p_descripcion_rol", rol.getDescripcion());

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_usuario", parametrosEntrada, parametrosSalida);
        rol.setRolID((int)parametrosSalida.get("p_id_generado"));

        return rol.getRolID();
    }
    @Override
    public int modificar(Rol objeto){

        int cantidad=0;
        try{
            con = DBManager.getDBManager().getConnection();
            cs= con.prepareCall("{call pa_modificar_rol(?, ?, ?)}");
            cs.setInt("p_id_rol", objeto.getRolID());
            cs.setString("p_titulo_rol", objeto.getTitulo());
            cs.setString("p_descripcion_rol", objeto.getDescripcion());

            cantidad = cs.executeUpdate();
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
        return cantidad;
    }
    @Override
    public int eliminar(int idRol){
        int eliminados=0;
        try{
            con = DBManager.getDBManager().getConnection();
            cs= con.prepareCall("{call pa_eliminar_rol(?)}");
            cs.setInt("p_id_rol",idRol);
            eliminados=cs.executeUpdate();
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
        return eliminados;
    }
    @Override
    public Rol buscarPorId(int idRol){
        Rol rol = null;
        try{
            con = DBManager.getDBManager().getConnection();
            cs= con.prepareCall("{call pa_buscar_rol_por_id(?)}");
            cs.setInt("p_id_rol",idRol);
            rs=cs.executeQuery();

            if(rs.next()){
                rol=new Rol();
                rol.setDescripcion(rs.getString("descripcion_rol"));
                rol.setTitulo(rs.getString("titulo_rol"));
            }
        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }
        finally {
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
        return rol;
    }

    @Override
    public List<Rol> listarTodas(){
        List<Rol> roles= null;
        try{
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_listar_roles()}");
            rs=cs.executeQuery();

            while(rs.next()){
                if(roles == null)
                    roles = new ArrayList<>();
                Rol rol = new Rol();
                rol.setRolID(rs.getInt("id_rol"));
                rol.setTitulo(rs.getString("titulo_rol"));
                rol.setDescripcion(rs.getString("descripcion_rol"));
                roles.add(rol);
            }
            return roles;
        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }
        finally {
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

        return roles;
    }
}


