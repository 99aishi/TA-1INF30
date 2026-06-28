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
import pe.edu.pucp.economix.operaciones.idao.ISolicitudGastoDAO;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.Rendicion;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoCicloCaja;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoSolicitudGasto;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.rrhh.model.RolFlujo;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

public class SolicitudGastoDAOImpl implements ISolicitudGastoDAO {
    private ResultSet rs;

    @Override
    public int insertar(SolicitudGasto solicitudGasto, int idUsuarioAccion) throws SQLException{
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_generado", Types.INTEGER);
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_fecha_solicitud", solicitudGasto.getFechaSolicitud());
        parametrosEntrada.put("p_monto_solicitado", solicitudGasto.getMontoSolicitado());
        if(solicitudGasto.getMonedaOriginal() != null)
            parametrosEntrada.put("p_id_moneda_original", solicitudGasto.getMonedaOriginal().getIdMoneda());
        else
            parametrosEntrada.put("p_id_moneda_original", null);
        parametrosEntrada.put("p_tipo_cambio", solicitudGasto.getTipoCambio());
        parametrosEntrada.put("p_monto_convertido", solicitudGasto.getMontoConvertido());
        parametrosEntrada.put("p_motivo_solicitud", solicitudGasto.getMotivoSolicitud());
        parametrosEntrada.put("p_estado_solicitud", solicitudGasto.getEstado().toString());
        parametrosEntrada.put("p_comentario_decision", solicitudGasto.getComentarioDecision());
        if(solicitudGasto.getIdTransaccion() > 0)
            parametrosEntrada.put("p_id_transaccion", solicitudGasto.getIdTransaccion());
        else
            parametrosEntrada.put("p_id_transaccion", null);
        if(solicitudGasto.getSolicitante() != null)
            parametrosEntrada.put("p_id_usuario_solicitante", solicitudGasto.getSolicitante().getUsuarioID());
        else
            parametrosEntrada.put("p_id_usuario_solicitante", null);
        if(solicitudGasto.getDestinatario() != null)
            parametrosEntrada.put("p_id_usuario_destinatario", solicitudGasto.getDestinatario().getUsuarioID());
        else
            parametrosEntrada.put("p_id_usuario_destinatario", null);
        if(solicitudGasto.getJefeAprobador() != null)
            parametrosEntrada.put("p_id_jefe_aprobador", solicitudGasto.getJefeAprobador().getUsuarioID());
        else
            parametrosEntrada.put("p_id_jefe_aprobador", null);
        if(solicitudGasto.getTesoreroAprobador() != null)
            parametrosEntrada.put("p_id_tesorero_aprobador", solicitudGasto.getTesoreroAprobador().getUsuarioID());
        else
            parametrosEntrada.put("p_id_tesorero_aprobador", null);
        if(solicitudGasto.getCiclo() != null)
            parametrosEntrada.put("p_id_ciclo_caja", solicitudGasto.getCiclo().getIdCicloCaja());
        else
            parametrosEntrada.put("p_id_ciclo_caja", null);

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_solicitud_gasto", parametrosEntrada, parametrosSalida);
        solicitudGasto.setIdSolicitudGasto((int)parametrosSalida.get("p_id_generado"));

        return solicitudGasto.getIdSolicitudGasto();
    }

    @Override
    public int modificar(SolicitudGasto solicitudGasto, int idUsuarioAccion) throws SQLException {
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_solicitud_gasto", solicitudGasto.getIdSolicitudGasto());
        parametrosEntrada.put("p_fecha_solicitud", solicitudGasto.getFechaSolicitud());
        parametrosEntrada.put("p_monto_solicitado", solicitudGasto.getMontoSolicitado());
        if(solicitudGasto.getMonedaOriginal() != null)
            parametrosEntrada.put("p_id_moneda_original", solicitudGasto.getMonedaOriginal().getIdMoneda());
        else
            parametrosEntrada.put("p_id_moneda_original", null);
        parametrosEntrada.put("p_tipo_cambio", solicitudGasto.getTipoCambio());
        parametrosEntrada.put("p_monto_convertido", solicitudGasto.getMontoConvertido());
        parametrosEntrada.put("p_motivo_solicitud", solicitudGasto.getMotivoSolicitud());
        parametrosEntrada.put("p_estado_solicitud", solicitudGasto.getEstado().toString());
        parametrosEntrada.put("p_comentario_decision", solicitudGasto.getComentarioDecision());
        if(solicitudGasto.getIdTransaccion() > 0)
            parametrosEntrada.put("p_id_transaccion", solicitudGasto.getIdTransaccion());
        else
            parametrosEntrada.put("p_id_transaccion", null);
        if(solicitudGasto.getSolicitante() != null)
            parametrosEntrada.put("p_id_usuario_solicitante", solicitudGasto.getSolicitante().getUsuarioID());
        else
            parametrosEntrada.put("p_id_usuario_solicitante", null);
        if(solicitudGasto.getDestinatario() != null)
            parametrosEntrada.put("p_id_usuario_destinatario", solicitudGasto.getDestinatario().getUsuarioID());
        else
            parametrosEntrada.put("p_id_usuario_destinatario", null);
        if(solicitudGasto.getJefeAprobador() != null)
            parametrosEntrada.put("p_id_jefe_aprobador", solicitudGasto.getJefeAprobador().getUsuarioID());
        else
            parametrosEntrada.put("p_id_jefe_aprobador", null);
        if(solicitudGasto.getTesoreroAprobador() != null)
            parametrosEntrada.put("p_id_tesorero_aprobador", solicitudGasto.getTesoreroAprobador().getUsuarioID());
        else
            parametrosEntrada.put("p_id_tesorero_aprobador", null);
        if(solicitudGasto.getCiclo() != null)
            parametrosEntrada.put("p_id_ciclo_caja", solicitudGasto.getCiclo().getIdCicloCaja());
        else
            parametrosEntrada.put("p_id_ciclo_caja", null);

        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_solicitud_gasto", parametrosEntrada, null);

        return resultado;
    }

    @Override
    public int eliminar(int idSolicitudGasto, int idUsuarioAccion) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_solicitud_gasto", idSolicitudGasto);
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_eliminar_solicitud_gasto", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public SolicitudGasto buscarPorId(int idSolicitudGasto) throws SQLException {
        SolicitudGasto solicitud = null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_solicitud_gasto", idSolicitudGasto);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_buscar_solicitud_gasto_por_id", parametrosEntrada);
        try{
            if(rs.next()){
                solicitud = mapearSolicitudGastoPorId(rs, new HashMap<>());
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar solicitud por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return solicitud;
    }

    @SuppressWarnings("unchecked")
    private <T> T getOrCreate(Map<Class<?>, Map<Integer, Object>> cache, Class<T> type, int id, Supplier<T> factory) {
        if (id <= 0) return null;
        return (T) cache.computeIfAbsent(type, k -> new HashMap<>())
                .computeIfAbsent(id, k -> factory.get());
    }

    private SolicitudGasto mapearSolicitudGastoPorId(ResultSet rs, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt("id_solicitud_gasto");
        SolicitudGasto solicitud = getOrCreate(cache, SolicitudGasto.class, id, () -> new SolicitudGasto());
        solicitud.setIdSolicitudGasto(id);
        solicitud.setFechaSolicitud(rs.getTimestamp("fecha_solicitud"));
        solicitud.setMontoSolicitado(rs.getDouble("monto_solicitado"));

        Moneda moneda = mapearMoneda(rs, "mon_id_moneda", "mon_", cache);
        if (moneda != null) {
            solicitud.setMonedaOriginal(moneda);
        }

        solicitud.setTipoCambio(rs.getDouble("tipo_cambio"));
        solicitud.setMontoConvertido(rs.getDouble("monto_convertido"));
        solicitud.setMotivoSolicitud(rs.getString("motivo_solicitud"));
        solicitud.setEstado(EstadoSolicitudGasto.valueOf(rs.getString("estado_solicitud")));
        solicitud.setComentarioDecision(rs.getString("comentario_decision"));
        int idTransaccion = rs.getInt("id_transaccion");
        if(!rs.wasNull())
            solicitud.setIdTransaccion(idTransaccion);

        solicitud.setSolicitante(mapearEmpleadoBasico(rs, "sol_id_usuario", "sol_", cache));
        solicitud.setDestinatario(mapearEmpleadoBasico(rs, "des_id_usuario", "des_", cache));
        solicitud.setJefeAprobador(mapearEmpleadoBasico(rs, "jefe_id_usuario", "jefe_", cache));
        solicitud.setTesoreroAprobador(mapearEmpleadoBasico(rs, "tes_id_usuario", "tes_", cache));

        CicloCajaChica ciclo = mapearCicloCajaChicaBasico(rs, "cc_", cache);
        if (ciclo != null) {
            solicitud.setCiclo(ciclo);
        }

        return solicitud;
    }

    private Empleado crearEmpleadoStub(ResultSet rs, String idColumna, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt(idColumna);
        if (rs.wasNull() || id <= 0) return null;
        Empleado empleado = getOrCreate(cache, Empleado.class, id, () -> new Empleado());
        empleado.setUsuarioID(id);
        return empleado;
    }

    private Moneda mapearMoneda(ResultSet rs, String idColumna, String prefijo,
                                Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt(idColumna);
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

    private Empleado mapearEmpleadoBasico(ResultSet rs, String idColumna, String prefijo,
                                          Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt(idColumna);
        if (rs.wasNull() || id <= 0) return null;
        return getOrCreate(cache, Empleado.class, id, () -> {
            Empleado empleado = new Empleado();
            empleado.setUsuarioID(id);
            try {
                empleado.setNombres(rs.getString(prefijo + "nombres"));
                empleado.setApellidoPaterno(rs.getString(prefijo + "apellido_paterno"));
                empleado.setApellidoMaterno(rs.getString(prefijo + "apellido_materno"));
                empleado.setCorreo(rs.getString(prefijo + "correo"));
                empleado.setNumeroCelular(rs.getString(prefijo + "numero_celular"));
                String rolFlujo = rs.getString(prefijo + "rol_flujo");
                if (rolFlujo != null)
                    empleado.setRolFlujo(RolFlujo.valueOf(rolFlujo));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return empleado;
        });
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
            ciclo.setCajaChica(cc);
        }

        int idRendicion = rs.getInt(prefijo + "id_rendicion");
        if (!rs.wasNull() && idRendicion > 0) {
            Rendicion ren = getOrCreate(cache, Rendicion.class, idRendicion, () -> new Rendicion());
            ren.setIdRendicion(idRendicion);
            ciclo.setRendicion(ren);
        }

        return ciclo;
    }

    public List<SolicitudGasto> listarPorCiclo(int idCicloCaja) throws SQLException{
        List<SolicitudGasto> solicitudes = null;
        SolicitudGasto solicitud;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_ciclo_caja", idCicloCaja);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_solicitudes_por_ciclo", parametrosEntrada);
        try{
            while(rs.next()){
                if(solicitudes == null) solicitudes = new ArrayList<>();
                solicitud = mapearSolicitudGastoPorId(rs, new HashMap<>());
                solicitudes.add(solicitud);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar solicitudes por ciclo: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return solicitudes;
    }

    @Override
    public List<SolicitudGasto> listarTodas() throws SQLException {
        List<SolicitudGasto> solicitudes = null;
        SolicitudGasto solicitud;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_solicitudes_gasto", null);
        try{
            while(rs.next()){
                if(solicitudes == null) solicitudes = new ArrayList<>();
                solicitud = mapearSolicitudGastoPorId(rs, new HashMap<>());
                solicitudes.add(solicitud);
            }
        }catch(SQLException ex){
            System.out.println("Error al listar solicitudes: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }

        return solicitudes;
    }

    @Override
    public List<SolicitudGasto> listarActivas() throws SQLException {
        List<SolicitudGasto> solicitudes = null;
        SolicitudGasto solicitud;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_solicitudes_activas", null);
        try{
            while(rs.next()){
                if(solicitudes == null) solicitudes = new ArrayList<>();
                solicitud = mapearSolicitudGastoPorId(rs, new HashMap<>());
                solicitudes.add(solicitud);
            }
        }catch(SQLException ex){
            System.out.println("Error al listar solicitudes activas: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return solicitudes;
    }

    @Override
    public List<SolicitudGasto> listarPorSolicitante(int idUsuarioSolicitante) throws SQLException {
        List<SolicitudGasto> solicitudes = null;
        SolicitudGasto solicitud;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_solicitante", idUsuarioSolicitante);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_solicitudes_por_solicitante", parametrosEntrada);
        try{
            while(rs.next()){
                if(solicitudes == null) solicitudes = new ArrayList<>();
                solicitud = mapearSolicitudGastoPorId(rs, new HashMap<>());
                solicitudes.add(solicitud);
            }
        }catch(SQLException ex){
            System.out.println("Error al listar solicitudes por solicitante: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return solicitudes;
    }

    @Override
    public List<SolicitudGasto> listarPendientesJefe(int idUsuarioDestinatario) throws SQLException {
        List<SolicitudGasto> solicitudes = null;
        SolicitudGasto solicitud;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_destinatario", idUsuarioDestinatario);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_solicitudes_pendientes_jefe", parametrosEntrada);
        try{
            while(rs.next()){
                if(solicitudes == null) solicitudes = new ArrayList<>();
                solicitud = mapearSolicitudGastoPorId(rs, new HashMap<>());
                solicitudes.add(solicitud);
            }
        }catch(SQLException ex){
            System.out.println("Error al listar solicitudes pendientes de jefe: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return solicitudes;
    }
}
