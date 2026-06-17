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
import pe.edu.pucp.economix.operaciones.idao.ICicloCajaChicaDAO;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoCicloCaja;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoRendicion;
import pe.edu.pucp.economix.operaciones.model.Rendicion;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;
import pe.edu.pucp.economix.tesoreria.model.EstadoFondo;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

public class CicloCajaChicaDAOImpl implements ICicloCajaChicaDAO {
    private ResultSet rs;

    @Override
    public int insertar(CicloCajaChica cicloCajaChica, int idUsuarioAccion) throws SQLException {
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_generado", Types.INTEGER);
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_numero_semana", cicloCajaChica.getNumeroSemana());
        if(cicloCajaChica.getFechaApertura()!= null)
            parametrosEntrada.put("p_fecha_apertura", new java.sql.Date(cicloCajaChica.getFechaApertura().getTime()));
        else
            parametrosEntrada.put("p_fecha_apertura", null);

        if(cicloCajaChica.getFechaCierre()!= null)
            parametrosEntrada.put("p_fecha_cierre", new java.sql.Date(cicloCajaChica.getFechaCierre().getTime()));
        else
            parametrosEntrada.put("p_fecha_cierre", null);
        parametrosEntrada.put("p_monto_saldo_inicial", cicloCajaChica.getSaldoInicial());
        parametrosEntrada.put("p_monto_total_gastado", cicloCajaChica.getTotalGastado());
        parametrosEntrada.put("p_estado_ciclo", cicloCajaChica.getEstado().toString());
        if(cicloCajaChica.getCajaChica() != null)
            parametrosEntrada.put("p_id_caja_chica", cicloCajaChica.getCajaChica().getIdFondo());
        else
            parametrosEntrada.put("p_id_caja_chica", null);
        if(cicloCajaChica.getRendicion()!= null)
            parametrosEntrada.put("p_id_rendicion", cicloCajaChica.getRendicion().getIdRendicion());
        else
            parametrosEntrada.put("p_id_rendicion", null);

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_ciclo_caja", parametrosEntrada, parametrosSalida);
        cicloCajaChica.setIdCicloCaja((int)parametrosSalida.get("p_id_generado"));

        return cicloCajaChica.getIdCicloCaja();
    }

    @Override
    public int modificar(CicloCajaChica cicloCajaChica, int idUsuarioAccion) throws SQLException{
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_ciclo_caja", cicloCajaChica.getIdCicloCaja());
        parametrosEntrada.put("p_numero_semana", cicloCajaChica.getNumeroSemana());
        if(cicloCajaChica.getFechaApertura()!= null)
            parametrosEntrada.put("p_fecha_apertura", new java.sql.Date(cicloCajaChica.getFechaApertura().getTime()));
        else
            parametrosEntrada.put("p_fecha_apertura", null);
        if(cicloCajaChica.getFechaCierre()!= null)
            parametrosEntrada.put("p_fecha_cierre", new java.sql.Date(cicloCajaChica.getFechaCierre().getTime()));
        else
            parametrosEntrada.put("p_fecha_cierre", null);
        parametrosEntrada.put("p_monto_saldo_inicial", cicloCajaChica.getSaldoInicial());
        parametrosEntrada.put("p_monto_total_gastado", cicloCajaChica.getTotalGastado());
        parametrosEntrada.put("p_estado_ciclo", cicloCajaChica.getEstado().toString());
        if(cicloCajaChica.getCajaChica() != null)
            parametrosEntrada.put("p_id_caja_chica", cicloCajaChica.getCajaChica().getIdFondo());
        else
            parametrosEntrada.put("p_id_caja_chica", null);
        if(cicloCajaChica.getRendicion()!= null)
            parametrosEntrada.put("p_id_rendicion", cicloCajaChica.getRendicion().getIdRendicion());
        else
            parametrosEntrada.put("p_id_rendicion", null);

        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_ciclo_caja", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public int eliminar(int idCicloCaja, int idUsuarioAccion) throws SQLException{
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_ciclo_caja", idCicloCaja);
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_eliminar_ciclo_caja", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public CicloCajaChica buscarPorId(int idCicloCaja) throws SQLException{
        CicloCajaChica ciclo = null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_ciclo_caja", idCicloCaja);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_buscar_ciclo_caja_por_id", parametrosEntrada);
        try{
            if(rs.next()){
                ciclo = mapearCicloCajaChicaPorId(rs, new HashMap<>());
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar ciclo caja chica por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return ciclo;
    }

    @SuppressWarnings("unchecked")
    private <T> T getOrCreate(Map<Class<?>, Map<Integer, Object>> cache, Class<T> type, int id, Supplier<T> factory) {
        if (id <= 0) return null;
        return (T) cache.computeIfAbsent(type, k -> new HashMap<>())
                .computeIfAbsent(id, k -> factory.get());
    }

    private CicloCajaChica mapearCicloCajaChicaPorId(ResultSet rs, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt("id_ciclo_caja");
        CicloCajaChica ciclo = getOrCreate(cache, CicloCajaChica.class, id, () -> new CicloCajaChica());
        ciclo.setIdCicloCaja(id);
        ciclo.setNumeroSemana(rs.getInt("numero_semana"));
        ciclo.setFechaApertura(rs.getDate("fecha_apertura"));
        ciclo.setFechaCierre(rs.getDate("fecha_cierre"));
        ciclo.setSaldoInicial(rs.getDouble("monto_saldo_inicial"));
        ciclo.setTotalGastado(rs.getDouble("monto_total_gastado"));
        ciclo.setEstado(EstadoCicloCaja.valueOf(rs.getString("estado_ciclo")));

        CajaChica ccj = mapearCajaChica(rs, "ccj_", cache);
        if (ccj != null) {
            ciclo.setCajaChica(ccj);
        }

        Rendicion ren = mapearRendicion(rs, "ren_", cache);
        if (ren != null) {
            ren.setCicloCajaChica(ciclo);
            ciclo.setRendicion(ren);
        }

        return ciclo;
    }

    private CajaChica mapearCajaChica(ResultSet rs, String prefijo, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt(prefijo + "id_fondo");
        if (rs.wasNull() || id <= 0) return null;
        CajaChica cc = getOrCreate(cache, CajaChica.class, id, () -> new CajaChica());
        cc.setIdFondo(id);
        cc.setNombre(rs.getString(prefijo + "nombre_fondo"));
        cc.setMontoTecho(rs.getDouble(prefijo + "monto_techo"));
        String estadoFondo = rs.getString(prefijo + "estado_fondo");
        if(estadoFondo != null)
            cc.setEstado(EstadoFondo.valueOf(estadoFondo));

        int idArea = rs.getInt(prefijo + "id_area");
        if (!rs.wasNull() && idArea > 0) {
            Area area = getOrCreate(cache, Area.class, idArea, () -> new Area());
            area.setIdArea(idArea);
            cc.setAreaAsignada(area);
        }

        int idMoneda = rs.getInt(prefijo + "id_moneda");
        if (!rs.wasNull() && idMoneda > 0) {
            Moneda moneda = getOrCreate(cache, Moneda.class, idMoneda, () -> new Moneda());
            moneda.setIdMoneda(idMoneda);
            cc.setMoneda(moneda);
        }

        int idCuentaOrigen = rs.getInt(prefijo + "id_cuenta_origen");
        if (!rs.wasNull() && idCuentaOrigen > 0) {
            CuentaBancaria cb = getOrCreate(cache, CuentaBancaria.class, idCuentaOrigen, () -> new CuentaBancaria());
            cb.setIdCuenta(idCuentaOrigen);
            cc.setCuentaOrigen(cb);
        }

        return cc;
    }

    private Rendicion mapearRendicion(ResultSet rs, String prefijo, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt(prefijo + "id_rendicion");
        if (rs.wasNull() || id <= 0) return null;
        Rendicion ren = getOrCreate(cache, Rendicion.class, id, () -> new Rendicion());
        ren.setIdRendicion(id);
        ren.setFechaPresentacion(rs.getDate(prefijo + "fecha_presentacion"));
        ren.setFechaAprobacion(rs.getDate(prefijo + "fecha_aprobacion"));
        ren.setTotalDeclarado(rs.getDouble(prefijo + "monto_total_declarado"));
        ren.setTotalAprobado(rs.getDouble(prefijo + "monto_total_aprobado"));
        ren.setSaldoFinal(rs.getDouble(prefijo + "monto_saldo_final"));
        String estadoRendicion = rs.getString(prefijo + "estado_rendicion");
        if(estadoRendicion != null)
            ren.setEstado(EstadoRendicion.valueOf(estadoRendicion));
        ren.setComentario(rs.getString(prefijo + "comentario"));
        return ren;
    }

    @Override
    public List<CicloCajaChica> listarTodas() throws SQLException{
        List<CicloCajaChica> ciclos = null;
        CicloCajaChica ciclo;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_todos_ciclos_caja", null);
        try{
            while(rs.next()){
                if(ciclos == null) ciclos = new ArrayList<>();
                ciclo = mapearCicloCajaChicaPorId(rs, new HashMap<>());
                ciclos.add(ciclo);
            }
        }catch(SQLException ex){
            System.out.println("Error al listar ciclos caja chica: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }

        return ciclos;
    }

    @Override
    public List<CicloCajaChica> listarActivos() throws SQLException{
        List<CicloCajaChica> ciclos = null;
        CicloCajaChica ciclo;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_ciclos_activos", null);
        try{
            while(rs.next()){
                if(ciclos == null) ciclos = new ArrayList<>();
                ciclo = mapearCicloCajaChicaPorId(rs, new HashMap<>());
                ciclos.add(ciclo);
            }
        }catch(SQLException ex){
            System.out.println("Error al listar ciclos activos: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return ciclos;
    }

    @Override
    public List<CicloCajaChica> listarPasados() throws SQLException{
        List<CicloCajaChica> ciclos = null;
        CicloCajaChica ciclo;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_ciclos_pasados", null);
        try{
            while(rs.next()){
                if(ciclos == null) ciclos = new ArrayList<>();
                ciclo = mapearCicloCajaChicaPorId(rs, new HashMap<>());
                ciclos.add(ciclo);
            }
        }catch(SQLException ex){
            System.out.println("Error al listar ciclos pasados: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return ciclos;
    }
}
