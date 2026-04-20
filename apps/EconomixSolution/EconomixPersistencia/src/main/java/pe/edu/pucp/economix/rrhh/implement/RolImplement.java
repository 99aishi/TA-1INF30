package pe.edu.pucp.economix.rrhh.implement;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.rrhh.dao.IRolDAO;
import pe.edu.pucp.economix.rrhh.model.Rol;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolImplement  implements IRolDAO{
    private Connection con;
    private ResultSet rs;
    private CallableStatement cs;

    @Override
    public int insertar(Rol objeto){

        int id=0;
        try{
            con = DBManager.getDBManager().getConnection();
            cs= con.prepareCall("{call pa_insertar_rol(?,?,?)}");
            cs.setString("p_titulo_rol", objeto.getTitulo());
            cs.setString("p_descripcion_rol", objeto.getDescripcion());
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


