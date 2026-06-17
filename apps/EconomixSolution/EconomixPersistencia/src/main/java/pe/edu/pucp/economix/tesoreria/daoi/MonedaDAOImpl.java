package pe.edu.pucp.economix.tesoreria.daoi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.tesoreria.idao.IMonedaDAO;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

public class MonedaDAOImpl implements IMonedaDAO {
    private ResultSet rs;

    @Override
    public int insertar(Moneda moneda, int idUsuarioAccion) throws SQLException {
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_moneda", Types.INTEGER);
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_codigo_iso", moneda.getCodigoISO());
        parametrosEntrada.put("p_simbolo", moneda.getSimbolo());
        parametrosEntrada.put("p_nombre", moneda.getNombre());
        parametrosEntrada.put("p_descripcion", moneda.getDescripcion());

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_moneda", parametrosEntrada, parametrosSalida);
        moneda.setIdMoneda((int)parametrosSalida.get("p_id_moneda"));

        return moneda.getIdMoneda();
    }

    @Override
    public int modificar(Moneda moneda, int idUsuarioAccion) throws SQLException {
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_moneda", moneda.getIdMoneda());
        parametrosEntrada.put("p_codigo_iso", moneda.getCodigoISO());
        parametrosEntrada.put("p_simbolo", moneda.getSimbolo());
        parametrosEntrada.put("p_nombre", moneda.getNombre());
        parametrosEntrada.put("p_descripcion", moneda.getDescripcion());
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_moneda", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public int eliminar(int idMoneda, int idUsuarioAccion) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_moneda", idMoneda);
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_eliminar_moneda", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public Moneda buscarPorId(int id) throws SQLException {
        Moneda moneda = null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_moneda", id);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_buscar_moneda_por_id", parametrosEntrada);
        try {
            if (rs.next()) {
                moneda = mapearMoneda(rs, new HashMap<>());
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar moneda por id: " + ex.getMessage());
        } finally {
            DBManager.getDBManager().cerrarConexion();
        }
        return moneda;
    }


    @Override
    public List<Moneda> listarTodas() throws SQLException {
        List<Moneda>monedas=null;
        Moneda moneda;
        Map<Class<?>, Map<Integer, Object>> cache = new HashMap<>();
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_monedas", null);
        try{
            while(rs.next()){
                if(monedas == null) monedas = new ArrayList<>();
                moneda = mapearMoneda(rs, cache);
                monedas.add(moneda);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar monedas: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return monedas;
    }

    @Override
    public List<Moneda> listarMonedas_X_codigoISO_nombre_simbolo(String busqueda) throws SQLException{
        ArrayList<Moneda> monedas = new ArrayList<>();
        Map<Class<?>, Map<Integer, Object>> cache = new HashMap<>();

        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_comentario_busqueda", busqueda);

        rs = DBManager.getDBManager().ejecutarProcedimientoLectura(
                "pa_listar_monedas_X_codigoISO_nombre_simbolo",
                parametrosEntrada
        );

        try {
            while (rs.next()) {
                monedas.add(mapearMoneda(rs, cache));
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar monedas por código ISO, nombre o símbolo: " + ex.getMessage());
        } finally {
            DBManager.getDBManager().cerrarConexion();
        }

        return monedas;
    }

    @Override
    public List<Moneda> listarActivas() throws SQLException {
        return listarMonedas_X_estado(true);
    }

    @Override
    public int recuperar(int idMoneda, int idUsuarioAccion) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_moneda", idMoneda);
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_reactivar_moneda", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public List<Moneda> listarMonedas_X_estado(boolean activa) throws SQLException {
        ArrayList<Moneda> monedas = new ArrayList<>();
        Map<Class<?>, Map<Integer, Object>> cache = new HashMap<>();

        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_activa", activa ? 1 : 0);

        rs = DBManager.getDBManager().ejecutarProcedimientoLectura(
                "pa_listar_monedas_por_estado",
                parametrosEntrada
        );

        try {
            while (rs.next()) {
                monedas.add(mapearMoneda(rs, cache));
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar monedas por estado: " + ex.getMessage());
        } finally {
            DBManager.getDBManager().cerrarConexion();
        }

        return monedas;
    }

    @SuppressWarnings("unchecked")
    private <T> T getOrCreate(Map<Class<?>, Map<Integer, Object>> cache, Class<T> type, int id, Supplier<T> factory) {
        if (id <= 0) return null;
        return (T) cache.computeIfAbsent(type, k -> new HashMap<>())
                .computeIfAbsent(id, k -> factory.get());
    }

    private Moneda mapearMoneda(ResultSet rs, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt("id_moneda");
        if (rs.wasNull() || id <= 0) return null;
        return getOrCreate(cache, Moneda.class, id, () -> {
            Moneda moneda = new Moneda();
            moneda.setIdMoneda(id);
            try {
                moneda.setCodigoISO(rs.getString("codigo_iso"));
                moneda.setSimbolo(rs.getString("simbolo"));
                moneda.setNombre(rs.getString("nombre_moneda"));
                moneda.setDescripcion(rs.getString("descripcion"));
                moneda.setActiva(rs.getBoolean("activa"));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return moneda;
        });
    }
}
