package pe.edu.pucp.economix.rrhh.implement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.rrhh.dao.IAdministradorDAO;
import pe.edu.pucp.economix.rrhh.model.Administrador;
import pe.edu.pucp.economix.rrhh.model.EstadoUsuario;

public class AdministradorImplement implements IAdministradorDAO {
    private ResultSet rs;

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
    public int eliminar(int idAdmin) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario", idAdmin);
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_eliminar_administrador", parametrosEntrada, null);
        return resultado;
    }
    @Override
    public Administrador buscarPorId(int idAdmin) throws SQLException {
        Administrador administrador = null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario", idAdmin);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_buscar_administrador_por_id", parametrosEntrada);
        try{
            if(rs.next()){
                administrador = new Administrador();
                administrador.setUsuarioID(rs.getInt("id_usuario"));
                administrador.setCorreoSoporte(rs.getString("correo_soporte"));
                administrador.setEstado(EstadoUsuario.Activo);
                administrador.setPassword(rs.getString("password_hash"));
                administrador.setNombres(rs.getString("nombres"));
                administrador.setApellidoPaterno(rs.getString("apellido_paterno"));
                administrador.setApellidoMaterno(rs.getString("apellido_materno"));
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar administrador por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return administrador;
    }
    @Override
    public List<Administrador> listarTodas() throws SQLException {
        List<Administrador> administradores = null;
        Administrador administrador;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_administradores", null);
        try{
            while(rs.next()){
                if(administradores == null) administradores = new ArrayList<>();
                administrador = new Administrador();
                administrador.setUsuarioID(rs.getInt("id_usuario"));
                administrador.setCorreoSoporte(rs.getString("correo_soporte"));
                administrador.setEstado(EstadoUsuario.Activo);
                administrador.setPassword(rs.getString("password_hash"));
                administrador.setNombres(rs.getString("nombres"));
                administrador.setApellidoPaterno(rs.getString("apellido_paterno"));
                administrador.setApellidoMaterno(rs.getString("apellido_materno"));
                administradores.add(administrador);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar administradores: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return administradores;
    }
}
