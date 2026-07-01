package pe.edu.pucp.economix.operaciones.daoi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.operaciones.idao.IRendicionDAO;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoCicloCaja;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoRendicion;
import pe.edu.pucp.economix.operaciones.model.Rendicion;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;
import pe.edu.pucp.economix.tesoreria.model.EstadoFondo;

public class RendicionDAOImpl implements IRendicionDAO{
    private ResultSet rs;

    @Override
    public int insertar(Rendicion rendicion, int idUsuarioAccion) throws SQLException{
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_generado", Types.INTEGER);
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_fecha_presentacion", rendicion.getFechaPresentacion() != null
            ? new java.sql.Date(rendicion.getFechaPresentacion().getTime()) : null);
        parametrosEntrada.put("p_fecha_aprobacion", rendicion.getFechaAprobacion() != null
            ? new java.sql.Date(rendicion.getFechaAprobacion().getTime()) : null);
        parametrosEntrada.put("p_monto_total_declarado", rendicion.getTotalDeclarado());
        parametrosEntrada.put("p_monto_total_aprobado", rendicion.getTotalAprobado());
        parametrosEntrada.put("p_monto_saldo_final", rendicion.getSaldoFinal());
        parametrosEntrada.put("p_estado_rendicion", rendicion.getEstado().toString());
        parametrosEntrada.put("p_comentario", rendicion.getComentario());
        if(rendicion.getCicloCajaChica() != null)
            parametrosEntrada.put("p_id_ciclo_caja", rendicion.getCicloCajaChica().getIdCicloCaja());
        else
            parametrosEntrada.put("p_id_ciclo_caja", null);

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_rendicion", parametrosEntrada, parametrosSalida);
        rendicion.setIdRendicion((int)parametrosSalida.get("p_id_generado"));

        return rendicion.getIdRendicion();
    }
    @Override
    public int modificar(Rendicion rendicion, int idUsuarioAccion) throws SQLException{
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_rendicion", rendicion.getIdRendicion());
        parametrosEntrada.put("p_fecha_presentacion", rendicion.getFechaPresentacion() != null
            ? new java.sql.Timestamp(rendicion.getFechaPresentacion().getTime()) : null);
        parametrosEntrada.put("p_fecha_aprobacion", rendicion.getFechaAprobacion() != null
            ? new java.sql.Timestamp(rendicion.getFechaAprobacion().getTime()) : null);
        parametrosEntrada.put("p_monto_total_declarado", rendicion.getTotalDeclarado());
        parametrosEntrada.put("p_monto_total_aprobado", rendicion.getTotalAprobado());
        parametrosEntrada.put("p_monto_saldo_final", rendicion.getSaldoFinal());
        parametrosEntrada.put("p_estado_rendicion", rendicion.getEstado().toString());
        parametrosEntrada.put("p_comentario", rendicion.getComentario());
        if(rendicion.getCicloCajaChica() != null)
            parametrosEntrada.put("p_id_ciclo_caja", rendicion.getCicloCajaChica().getIdCicloCaja());
        else
            parametrosEntrada.put("p_id_ciclo_caja", null);

        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_rendicion", parametrosEntrada, null);
        return resultado;
    }
    @Override
    public int eliminar(int idRendicion, int idUsuarioAccion) throws SQLException{
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_rendicion", idRendicion);
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_eliminar_rendicion", parametrosEntrada, null);
        return resultado;
    }
    @Override
    public Rendicion buscarPorId(int idRendicion) throws SQLException{
        Rendicion rendicion = null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_rendicion", idRendicion);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_buscar_rendicion_por_id", parametrosEntrada);
        try{
            if(rs.next()){
                rendicion = mapearRendicionPorId(rs, new HashMap<>());
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar rendicion por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }

        return rendicion;
    }

    @SuppressWarnings("unchecked")
    private <T> T getOrCreate(Map<Class<?>, Map<Integer, Object>> cache, Class<T> type, int id, Supplier<T> factory) {
        if (id <= 0) return null;
        return (T) cache.computeIfAbsent(type, k -> new HashMap<>())
                .computeIfAbsent(id, k -> factory.get());
    }

    private Rendicion mapearRendicionPorId(ResultSet rs, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt("id_rendicion");
        Rendicion rendicion = getOrCreate(cache, Rendicion.class, id, () -> new Rendicion());
        rendicion.setIdRendicion(id);
        rendicion.setFechaPresentacion(rs.getTimestamp("fecha_presentacion"));
        rendicion.setFechaAprobacion(rs.getTimestamp("fecha_aprobacion"));
        rendicion.setTotalDeclarado(rs.getDouble("monto_total_declarado"));
        rendicion.setTotalAprobado(rs.getDouble("monto_total_aprobado"));
        rendicion.setSaldoFinal(rs.getDouble("monto_saldo_final"));
        String estStr = rs.getString("estado_rendicion");
        if (estStr != null) {
            try {
                rendicion.setEstado(EstadoRendicion.valueOf(estStr.toUpperCase().trim()));
            } catch (Exception e) {
                rendicion.setEstado(EstadoRendicion.EN_ESPERA);
            }
        }
        rendicion.setComentario(rs.getString("comentario"));

        CicloCajaChica ciclo = mapearCicloCajaChicaBasico(rs, "cc_", cache);
        if (ciclo != null) {
            rendicion.setCicloCajaChica(ciclo);
        }

        return rendicion;
    }

    private CicloCajaChica mapearCicloCajaChicaBasico(ResultSet rs, String prefijo, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt(prefijo + "id_ciclo_caja");
        if (rs.wasNull() || id <= 0) return null;
        CicloCajaChica ciclo = getOrCreate(cache, CicloCajaChica.class, id, () -> new CicloCajaChica());
        ciclo.setIdCicloCaja(id);
        ciclo.setNumeroSemana(rs.getInt(prefijo + "numero_semana"));
        ciclo.setFechaApertura(rs.getTimestamp(prefijo + "fecha_apertura"));
        ciclo.setFechaCierre(rs.getTimestamp(prefijo + "fecha_cierre"));
        ciclo.setSaldoInicial(rs.getDouble(prefijo + "monto_saldo_inicial"));
        ciclo.setTotalGastado(rs.getDouble(prefijo + "monto_total_gastado"));
        String estadoCiclo = rs.getString(prefijo + "estado_ciclo");
        if(estadoCiclo != null)
            ciclo.setEstado(EstadoCicloCaja.valueOf(estadoCiclo));

        int idCajaChica = rs.getInt(prefijo + "id_caja_chica");
        if (!rs.wasNull() && idCajaChica > 0) {
            CajaChica cc = getOrCreate(cache, CajaChica.class, idCajaChica, () -> new CajaChica());
            cc.setIdFondo(idCajaChica);
            cc.setNombre(rs.getString("ccj_nombre_fondo"));
            cc.setMontoTecho(rs.getDouble("ccj_monto_techo"));
            String estadoFondo = rs.getString("ccj_estado_fondo");
            if (estadoFondo != null) {
                try {
                    cc.setEstado(EstadoFondo.valueOf(estadoFondo));
                } catch (Exception e) {}
            }
            int idCuentaBancaria = rs.getInt("ccj_id_cuenta_bancaria");
            if (!rs.wasNull() && idCuentaBancaria > 0) {
                pe.edu.pucp.economix.tesoreria.model.CuentaBancaria cb = getOrCreate(cache, pe.edu.pucp.economix.tesoreria.model.CuentaBancaria.class, idCuentaBancaria, () -> new pe.edu.pucp.economix.tesoreria.model.CuentaBancaria());
                cb.setIdCuenta(idCuentaBancaria);
                cc.setCuentaBancaria(cb);
            }
            int idMoneda = rs.getInt("ccj_id_moneda");
            if (!rs.wasNull() && idMoneda > 0) {
                pe.edu.pucp.economix.tesoreria.model.Moneda moneda = getOrCreate(cache, pe.edu.pucp.economix.tesoreria.model.Moneda.class, idMoneda, () -> new pe.edu.pucp.economix.tesoreria.model.Moneda());
                moneda.setIdMoneda(idMoneda);
                String simbolo = rs.getString("ccj_moneda_simbolo");
                if (simbolo != null) moneda.setSimbolo(simbolo);
                String nombreMoneda = rs.getString("ccj_moneda_nombre");
                if (nombreMoneda != null) moneda.setNombre(nombreMoneda);
                String codigoIso = rs.getString("ccj_moneda_codigo_iso");
                if (codigoIso != null) moneda.setCodigoISO(codigoIso);
                cc.setMoneda(moneda);
            }
            ciclo.setCajaChica(cc);
        }

        return ciclo;
    }

    @Override
    public List<Rendicion> listarTodas() throws SQLException{
        List<Rendicion> rendiciones = null;
        Rendicion rendicion;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_rendiciones", null);
        try{
            while(rs.next()){
                if(rendiciones == null) rendiciones = new ArrayList<>();
                rendicion = mapearRendicionPorId(rs, new HashMap<>());
                rendiciones.add(rendicion);
            }
        }catch(SQLException ex){
            System.out.println("Error al listar rendiciones: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return rendiciones;
    }

    @Override
    public List<Rendicion> listarActivas() throws SQLException{
        List<Rendicion> rendiciones = null;
        Rendicion rendicion;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_rendiciones_activas", null);
        try{
            while(rs.next()){
                if(rendiciones == null) rendiciones = new ArrayList<>();
                rendicion = mapearRendicionPorId(rs, new HashMap<>());
                rendiciones.add(rendicion);
            }
        }catch(SQLException ex){
            System.out.println("Error al listar rendiciones activas: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return rendiciones;
    }

    @Override
    public int cambiarEstadoRendicion(int idRendicion, String nuevoEstado, String comentario, int idUsuarioAccion) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_rendicion", idRendicion);
        parametrosEntrada.put("p_nuevo_estado", nuevoEstado);
        parametrosEntrada.put("p_comentario", comentario);
        return DBManager.getDBManager().ejecutarProcedimiento("pa_cambiar_estado_rendicion", parametrosEntrada, null);
    }

    @Override
    public List<Rendicion> listarPorArea(int idArea) throws SQLException {
        List<Rendicion> rendiciones = null;
        Rendicion rendicion;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_area", idArea);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_rendiciones_por_area", parametrosEntrada);
        try {
            while (rs.next()) {
                if (rendiciones == null) rendiciones = new ArrayList<>();
                rendicion = mapearRendicionPorId(rs, new HashMap<>());
                rendiciones.add(rendicion);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar rendiciones por area: " + ex.getMessage());
        } finally {
            DBManager.getDBManager().cerrarConexion();
        }
        return rendiciones;
    }

    @Override
    public int generarRendicionDeCicloSP(int idCiclo, int idUsuarioAccion) throws SQLException {
        int idGenerado = 0;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_ciclo_caja", idCiclo);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_generar_rendicion_de_ciclo", parametrosEntrada);
        try {
            if (rs.next()) {
                idGenerado = rs.getInt("id_generado");
            }
        } catch (SQLException ex) {
            System.out.println("Error al generar rendicion de ciclo: " + ex.getMessage());
        } finally {
            DBManager.getDBManager().cerrarConexion();
        }
        return idGenerado;
    }

}
