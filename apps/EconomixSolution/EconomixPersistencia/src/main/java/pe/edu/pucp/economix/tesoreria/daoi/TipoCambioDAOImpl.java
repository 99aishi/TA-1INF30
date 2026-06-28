package pe.edu.pucp.economix.tesoreria.daoi;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.tesoreria.idao.ITipoCambioDAO;
import pe.edu.pucp.economix.tesoreria.model.Moneda;
import pe.edu.pucp.economix.tesoreria.model.TipoCambio;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TipoCambioDAOImpl implements ITipoCambioDAO {
    private ResultSet rs;

    @Override
    public int insertar(TipoCambio tipoCambio, int idUsuarioAccion) throws SQLException {
        Map<String, Object> parametrosSalida = new HashMap<>();
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_generado", Types.INTEGER);
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_moneda_origen", tipoCambio.getMonedaOrigen().getIdMoneda());
        parametrosEntrada.put("p_id_moneda_destino", tipoCambio.getMonedaDestino().getIdMoneda());
        parametrosEntrada.put("p_valor_tipo_cambio", tipoCambio.getValor());
        parametrosEntrada.put("p_fecha_tipo_cambio", new java.sql.Date(tipoCambio.getFecha().getTime()));

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_tipo_cambio", parametrosEntrada, parametrosSalida);
        tipoCambio.setIdTipoCambio((int) parametrosSalida.get("p_id_generado"));

        return tipoCambio.getIdTipoCambio();
    }

    @Override
    public int modificar(TipoCambio tipoCambio, int idUsuarioAccion) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_tipo_cambio", tipoCambio.getIdTipoCambio());
        parametrosEntrada.put("p_id_moneda_origen", tipoCambio.getMonedaOrigen().getIdMoneda());
        parametrosEntrada.put("p_id_moneda_destino", tipoCambio.getMonedaDestino().getIdMoneda());
        parametrosEntrada.put("p_valor_tipo_cambio", tipoCambio.getValor());
        parametrosEntrada.put("p_fecha_tipo_cambio", new java.sql.Timestamp(tipoCambio.getFecha().getTime()));

        return DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_tipo_cambio", parametrosEntrada, null);
    }

    @Override
    public int eliminar(int idTipoCambio, int idUsuarioAccion) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_tipo_cambio", idTipoCambio);

        return DBManager.getDBManager().ejecutarProcedimiento("pa_eliminar_tipo_cambio", parametrosEntrada, null);
    }

    @Override
    public TipoCambio buscarPorId(int id) throws SQLException {
        TipoCambio tipoCambio = null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_tipo_cambio", id);

        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_buscar_tipo_cambio_por_id", parametrosEntrada);
        try {
            if (rs.next()) {
                tipoCambio = mapearTipoCambio(rs, new HashMap<>(), true);
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar tipo de cambio por id: " + ex.getMessage());
        } finally {
            DBManager.getDBManager().cerrarConexion();
        }
        return tipoCambio;
    }

    @Override
    public List<TipoCambio> listarTodas() throws SQLException {
        List<TipoCambio> tiposCambio = null;
        Map<Class<?>, Map<Integer, Object>> cache = new HashMap<>();
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_tipos_cambio", null);
        try {
            while (rs.next()) {
                if (tiposCambio == null) {
                    tiposCambio = new ArrayList<>();
                }
                tiposCambio.add(mapearTipoCambio(rs, cache, true));
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar tipos de cambio: " + ex.getMessage());
        } finally {
            DBManager.getDBManager().cerrarConexion();
        }
        return tiposCambio;
    }

    @Override
    public TipoCambio buscarPorMonedasYFecha(int idMonedaOrigen, int idMonedaDestino, java.sql.Date fecha) throws SQLException {
        TipoCambio tipoCambio = null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_moneda_origen", idMonedaOrigen);
        parametrosEntrada.put("p_id_moneda_destino", idMonedaDestino);
        parametrosEntrada.put("p_fecha", fecha);

        rs = DBManager.getDBManager().ejecutarProcedimientoLectura(
                "pa_buscar_tipo_cambio_por_monedas_fecha", parametrosEntrada);
        try {
            if (rs.next()) {
                tipoCambio = mapearTipoCambio(rs, new HashMap<>(), false);
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar tipo de cambio por monedas y fecha: " + ex.getMessage());
        } finally {
            DBManager.getDBManager().cerrarConexion();
        }
        return tipoCambio;
    }

    @SuppressWarnings("unchecked")
    private <T> T getOrCreate(Map<Class<?>, Map<Integer, Object>> cache, Class<T> type, int id, Supplier<T> factory) {
        if (id <= 0) return null;
        return (T) cache.computeIfAbsent(type, k -> new HashMap<>())
                .computeIfAbsent(id, k -> factory.get());
    }

    private TipoCambio mapearTipoCambio(ResultSet rs, Map<Class<?>, Map<Integer, Object>> cache, boolean monedasCompletas) throws SQLException {
        int id = rs.getInt("id_tipo_cambio");
        if (rs.wasNull() || id <= 0) return null;
        TipoCambio tipoCambio = getOrCreate(cache, TipoCambio.class, id, () -> new TipoCambio());
        tipoCambio.setIdTipoCambio(id);

        Moneda monedaOrigen;
        Moneda monedaDestino;
        if (monedasCompletas) {
            monedaOrigen = mapearMonedaCompleta(rs, "origen_", cache);
            monedaDestino = mapearMonedaCompleta(rs, "destino_", cache);
        } else {
            monedaOrigen = mapearMonedaPorId(rs, "id_moneda_origen", cache);
            monedaDestino = mapearMonedaPorId(rs, "id_moneda_destino", cache);
        }
        tipoCambio.setMonedaOrigen(monedaOrigen);
        tipoCambio.setMonedaDestino(monedaDestino);

        tipoCambio.setValor(rs.getDouble("valor_tipo_cambio"));
        tipoCambio.setFecha(rs.getTimestamp("fecha_tipo_cambio"));

        return tipoCambio;
    }

    private Moneda mapearMonedaCompleta(ResultSet rs, String prefijo, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt(prefijo + "id_moneda");
        if (rs.wasNull() || id <= 0) return null;
        return getOrCreate(cache, Moneda.class, id, () -> {
            Moneda moneda = new Moneda();
            moneda.setIdMoneda(id);
            try {
                moneda.setCodigoISO(rs.getString(prefijo + "codigo_iso"));
                moneda.setSimbolo(rs.getString(prefijo + "simbolo"));
                moneda.setNombre(rs.getString(prefijo + "nombre"));
                moneda.setDescripcion(rs.getString(prefijo + "descripcion"));
                moneda.setActiva(rs.getBoolean(prefijo + "activa"));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return moneda;
        });
    }

    private Moneda mapearMonedaPorId(ResultSet rs, String columnaId, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt(columnaId);
        if (rs.wasNull() || id <= 0) return null;
        return getOrCreate(cache, Moneda.class, id, () -> {
            Moneda moneda = new Moneda();
            moneda.setIdMoneda(id);
            return moneda;
        });
    }
}
