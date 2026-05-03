package pe.edu.pucp.economix.rrhh.implement;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.rrhh.dao.IAdministradorDAO;
import pe.edu.pucp.economix.rrhh.model.Administrador;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdministradorImplement implements IAdministradorDAO {
    private Connection con;
    private ResultSet rs;
    private CallableStatement cs;

    @Override
    public int insertar(Administrador administrador) throws SQLException {
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_generado", Types.INTEGER);
        parametrosEntrada.put("p_nombres", administrador.getNombres());
        parametrosEntrada.put("p_apellido_paterno", administrador.getApellidoPaterno());
        parametrosEntrada.put("p_apellido_materno", administrador.getApellidoMaterno());
        parametrosEntrada.put("p_password_hash", administrador.getPassword());

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_usuario", parametrosEntrada, parametrosSalida);
        administrador.setUsuarioID((int)parametrosSalida.get("p_id_generado"));

        parametrosSalida = new HashMap<>();
        parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario", administrador.getUsuarioID());
        parametrosEntrada.put("p_correo_soporte", administrador.getCorreoSoporte());
        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_administrador", parametrosEntrada, parametrosSalida);

        return administrador.getUsuarioID();
    }
    @Override
    public int modificar(Administrador administrador) throws SQLException {
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario", administrador.getUsuarioID());
        parametrosEntrada.put("p_nombres", administrador.getNombres());
        parametrosEntrada.put("p_apellido_paterno", administrador.getApellidoPaterno());
        parametrosEntrada.put("p_apellido_materno", administrador.getApellidoMaterno());
        parametrosEntrada.put("p_password_hash", administrador.getPassword());
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_usuario", parametrosEntrada, null);
        parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario", administrador.getUsuarioID());
        parametrosEntrada.put("p_id_correo_soporte", administrador.getCorreoSoporte());
        DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_administrador", parametrosEntrada, null);
        return resultado;
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
                    admin.setNombres(rsUsuario.getString("nombres"));
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
