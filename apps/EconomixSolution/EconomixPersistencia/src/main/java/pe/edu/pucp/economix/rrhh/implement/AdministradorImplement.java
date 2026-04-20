package pe.edu.pucp.economix.rrhh.implement;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.rrhh.dao.IAdministradorDAO;
import pe.edu.pucp.economix.rrhh.model.Administrador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministradorImplement implements IAdministradorDAO {
    private Connection con;
    private ResultSet rs;
    private CallableStatement cs;

    @Override
    public int insertar(Administrador administrador){
        int id=0;
        try{
            con = DBManager.getDBManager().getConnection();
            //Insertamos al usuario
            cs= con.prepareCall("{call pa_insertar_usuario(?,?,?,?,?)}");

            cs.setString("p_nombres", administrador.getNombre());
            cs.setString("p_apellido_paterno", administrador.getApellidoPaterno());
            cs.setString("p_apellido_materno", administrador.getApellidoMaterno());
            cs.setString("p_password_hash", administrador.getPassword());
            cs.registerOutParameter("p_id_generado", java.sql.Types.INTEGER); // TODO Revisión INTEGER

            cs.executeUpdate();

            id = cs.getInt("p_id_generado");


            //COn el usuario generado, insertamos al empleado
            cs= con.prepareCall("{call pa_insertar_administrador(?,?)}");
            cs.setInt("p_id_usuario", id);
            cs.setString("p_correo_soporte", administrador.getCorreoSoporte());

            cs.executeUpdate();

            return id;
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
    public int modificar(Administrador administrador){
        int cantidadPadre =0;
        int cantidadHijo = 0;
        try{
            con = DBManager.getDBManager().getConnection();
            //Insertamos al usuario
            cs= con.prepareCall("{call pa_modificar_usuario(?,?,?,?,?)}");

            cs.setInt("p_id_usuario", administrador.getUsuarioID());
            cs.setString("p_nombres", administrador.getNombre());
            cs.setString("p_apellido_paterno", administrador.getApellidoPaterno());
            cs.setString("p_apellido_materno", administrador.getApellidoMaterno());
            cs.setString("p_password_hash", administrador.getPassword());

            cantidadPadre = cs.executeUpdate();

            //COn el usuario generado, insertamos al empleado
            cs= con.prepareCall("{call pa_modificar_administrador(?,?)}");
            cs.setInt("p_id_usuario", administrador.getUsuarioID());
            cs.setString("p_correo_soporte", administrador.getCorreoSoporte());

            cantidadHijo = cs.executeUpdate();

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
        return cantidadPadre+cantidadHijo;
    }
    @Override
    public int eliminar(int idAdmin){
        int cantidad=0;
        try{
            con = DBManager.getDBManager().getConnection();
            //Insertamos al usuario
            cs= con.prepareCall("{call pa_eliminar_administrador(?)}");

            cs.setInt("p_id_usuario", idAdmin);
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
    public Administrador buscarPorId(int idAdmin){
        Administrador admin = null;
        try{
            con = DBManager.getDBManager().getConnection();
            //Insertamos al usuario
            cs= con.prepareCall("{call pa_buscar_administrador_por_id(?)}");
            cs.setInt("p_id_usuario", idAdmin);

            rs = cs.executeQuery();
            if(rs.next()){
                //Rescato los datose
                admin = new Administrador();
                admin.setUsuarioID(rs.getInt("id_usuario"));
                admin.setCorreoSoporte(rs.getString("correo_soporte"));
            }
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
        return  admin;
    }
    @Override
    public List<Administrador> listarTodas(){
        List<Administrador> administradores = null;
        Administrador admin = null;

        try{
            con = DBManager.getDBManager().getConnection();
            //Insertamos al usuario
            cs= con.prepareCall("{call pa_listar_administradores()}");
            rs = cs.executeQuery();
            while(rs.next()){
                if(administradores == null)
                    administradores = new ArrayList<>();
                //Rescato los datose
                admin = new Administrador();
                admin.setUsuarioID(rs.getInt("id_usuario"));
                admin.setCorreoSoporte(rs.getString("correo_soporte"));

                ResultSet rsUsuario;
                CallableStatement csUsuario= con.prepareCall("{call pa_buscar_usuario_por_id(?)}");
                int id = admin.getUsuarioID();
                csUsuario.setInt("p_id_usuario", id);
                rsUsuario = csUsuario.executeQuery();


                if(rsUsuario.next()){
                    admin.setNombre(rsUsuario.getString("nombres"));
                    admin.setApellidoPaterno(rsUsuario.getString("apellido_paterno"));
                    admin.setApellidoMaterno(rsUsuario.getString("apellido_materno"));
                }

                administradores.add(admin);
            }
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

        return administradores;
    }
}
