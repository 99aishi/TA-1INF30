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
import pe.edu.pucp.economix.rrhh.idao.IAdministradorDAO;
import pe.edu.pucp.economix.rrhh.model.Administrador;
import pe.edu.pucp.economix.rrhh.model.EstadoUsuario;

public class AdministradorDAOImpl implements IAdministradorDAO {
    private ResultSet rs;

    @Override
    public int insertar(Administrador administrador, int idUsuarioAccion) throws SQLException {
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_generado", Types.INTEGER);
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_nombres", administrador.getNombres());
        parametrosEntrada.put("p_apellido_paterno", administrador.getApellidoPaterno());
        parametrosEntrada.put("p_apellido_materno", administrador.getApellidoMaterno());
        parametrosEntrada.put("p_password_hash", administrador.getPassword());
        parametrosEntrada.put("p_correo", administrador.getCorreo());

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_usuario", parametrosEntrada, parametrosSalida);
        administrador.setUsuarioID((int)parametrosSalida.get("p_id_generado"));

        parametrosSalida = new HashMap<>();
        parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_usuario", administrador.getUsuarioID());
        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_administrador", parametrosEntrada, parametrosSalida);

        return administrador.getUsuarioID();
    }
    @Override
    public int modificar(Administrador administrador, int idUsuarioAccion) throws SQLException {
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_usuario", administrador.getUsuarioID());
        parametrosEntrada.put("p_nombres", administrador.getNombres());
        parametrosEntrada.put("p_apellido_paterno", administrador.getApellidoPaterno());
        parametrosEntrada.put("p_apellido_materno", administrador.getApellidoMaterno());
        parametrosEntrada.put("p_password_hash", administrador.getPassword());
        parametrosEntrada.put("p_correo", administrador.getCorreo());
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_usuario", parametrosEntrada, null);

        parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_usuario", administrador.getUsuarioID());
        DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_administrador", parametrosEntrada, null);
        return resultado;
    }
    @Override
    public int eliminar(int idAdmin, int idUsuarioAccion) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
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
                administrador = mapearAdministrador(rs, new HashMap<>());
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar administrador por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return administrador;
    }
    @Override
    public List<Administrador> listarActivas() throws SQLException {
        List<Administrador> administradores = null;
        Administrador administrador;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_administradores", null);
        try{
            while(rs.next()){
                if(administradores == null) administradores = new ArrayList<>();
                administrador = mapearAdministrador(rs, new HashMap<>());
                if (administrador != null) administradores.add(administrador);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar administradores: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return administradores;
    }
    @Override
    public List<Administrador> listarTodas() throws SQLException {
        List<Administrador> administradores = null;
        Administrador administrador;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_todos_administradores", null);
        try{
            while(rs.next()){
                if(administradores == null) administradores = new ArrayList<>();
                administrador = mapearAdministrador(rs, new HashMap<>());
                if (administrador != null) administradores.add(administrador);
            }
        }catch(SQLException ex){
            System.out.println("Error al listar administradores: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return administradores;
    }
    @Override
    public int verificarCuenta(String correo, String password)  throws  SQLException{
        int resultado=0;
        Administrador administrador = null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_correo", correo);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_obtener_password_administrador_por_correo", parametrosEntrada);
        try{
            if(rs.next()){
                administrador= new Administrador();
                administrador.setPasswordHash(rs.getString("password_hash"));
                if(administrador.validarPassword(password))resultado=1;
            }
        }catch(SQLException ex){
            System.out.println("Error al verificar cuenta" + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return resultado;
    }

    @SuppressWarnings("unchecked")
    private <T> T getOrCreate(Map<Class<?>, Map<Integer, Object>> cache, Class<T> type, int id, Supplier<T> factory) {
        if (id <= 0) return null;
        return (T) cache.computeIfAbsent(type, k -> new HashMap<>())
                .computeIfAbsent(id, k -> factory.get());
    }

    private Administrador mapearAdministrador(ResultSet rs, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt("id_usuario");
        if (rs.wasNull() || id <= 0) return null;
        Administrador administrador = getOrCreate(cache, Administrador.class, id, () -> new Administrador());
        administrador.setUsuarioID(id);
        administrador.setCorreo(rs.getString("correo"));
        String estadoStr = rs.getString("estado_usuario");
        administrador.setEstado(estadoStr != null ? EstadoUsuario.valueOf(estadoStr) : EstadoUsuario.ACTIVO);
        administrador.setPassword(rs.getString("password_hash"));
        administrador.setNombres(rs.getString("nombres"));
        administrador.setApellidoPaterno(rs.getString("apellido_paterno"));
        administrador.setApellidoMaterno(rs.getString("apellido_materno"));
        return administrador;
    }
}
