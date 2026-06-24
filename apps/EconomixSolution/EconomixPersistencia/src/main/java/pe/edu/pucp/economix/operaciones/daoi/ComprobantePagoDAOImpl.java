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
import pe.edu.pucp.economix.operaciones.idao.IComprobantePagoDAO;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.ComprobantePago;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoComprobante;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoSolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.enums.TipoComprobante;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

public class ComprobantePagoDAOImpl implements IComprobantePagoDAO {
    private ResultSet rs;

    @Override
    public int insertar(ComprobantePago comprobante, int idUsuarioAccion) throws SQLException {
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_generado", Types.INTEGER);
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_tipo_documento", comprobante.getTipoDocumento().toString());
        parametrosEntrada.put("p_ruc_proveedor", comprobante.getRUCProveedor());
        parametrosEntrada.put("p_razon_social", comprobante.getRazonSocial());
        parametrosEntrada.put("p_numero_serie", comprobante.getNumeroSerial());
        if (comprobante.getFechaEmision() != null)
            parametrosEntrada.put("p_fecha_emision", new java.sql.Date(comprobante.getFechaEmision().getTime()));
        else
            parametrosEntrada.put("p_fecha_emision", null);
        parametrosEntrada.put("p_monto_subtotal", comprobante.getSubtotal());
        parametrosEntrada.put("p_monto_igv", comprobante.getIgv());
        parametrosEntrada.put("p_monto_total", comprobante.getMontoTotal());
        parametrosEntrada.put("p_tipo_cambio", comprobante.getTipoCambio());
        parametrosEntrada.put("p_monto_convertido", comprobante.getMontoConvertido());
        parametrosEntrada.put("p_nombre_archivo", comprobante.getNombreArchivoComprobante());
        parametrosEntrada.put("p_estado_comprobante", comprobante.getEstado().toString());
        if (comprobante.getSolicitud() != null)
            parametrosEntrada.put("p_id_solicitud_gasto", comprobante.getSolicitud().getIdSolicitudGasto());
        else
            parametrosEntrada.put("p_id_solicitud_gasto", null);
        if (comprobante.getMoneda() != null)
            parametrosEntrada.put("p_id_moneda", comprobante.getMoneda().getIdMoneda());
        else
            parametrosEntrada.put("p_id_moneda", null);

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_comprobante_pago", parametrosEntrada, parametrosSalida);
        comprobante.setIdComprobante((int)parametrosSalida.get("p_id_generado"));

        return comprobante.getIdComprobante();
    }

    @Override
    public int modificar(ComprobantePago comprobante, int idUsuarioAccion) throws SQLException {
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_comprobante", comprobante.getIdComprobante());
        parametrosEntrada.put("p_tipo_documento", comprobante.getTipoDocumento().toString());
        parametrosEntrada.put("p_ruc_proveedor", comprobante.getRUCProveedor());
        parametrosEntrada.put("p_razon_social", comprobante.getRazonSocial());
        parametrosEntrada.put("p_numero_serie", comprobante.getNumeroSerial());
        if (comprobante.getFechaEmision() != null)
            parametrosEntrada.put("p_fecha_emision", new java.sql.Date(comprobante.getFechaEmision().getTime()));
        else
            parametrosEntrada.put("p_fecha_emision", null);
        parametrosEntrada.put("p_monto_subtotal", comprobante.getSubtotal());
        parametrosEntrada.put("p_monto_igv", comprobante.getIgv());
        parametrosEntrada.put("p_monto_total", comprobante.getMontoTotal());
        parametrosEntrada.put("p_tipo_cambio", comprobante.getTipoCambio());
        parametrosEntrada.put("p_monto_convertido", comprobante.getMontoConvertido());
        parametrosEntrada.put("p_nombre_archivo", comprobante.getNombreArchivoComprobante());
        parametrosEntrada.put("p_estado_comprobante", comprobante.getEstado().toString());
        if (comprobante.getSolicitud() != null)
            parametrosEntrada.put("p_id_solicitud_gasto", comprobante.getSolicitud().getIdSolicitudGasto());
        else
            parametrosEntrada.put("p_id_solicitud_gasto", null);
        if (comprobante.getMoneda() != null)
            parametrosEntrada.put("p_id_moneda", comprobante.getMoneda().getIdMoneda());
        else
            parametrosEntrada.put("p_id_moneda", null);

        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_comprobante_pago", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public int eliminar(int idComprobante, int idUsuarioAccion) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_comprobante", idComprobante);
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_eliminar_comprobante_pago", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public ComprobantePago buscarPorId(int idComprobante) throws SQLException {
        ComprobantePago comprobante = null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_comprobante", idComprobante);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_buscar_comprobante_pago_por_id", parametrosEntrada);
        try{
            if(rs.next()){
                comprobante = mapearComprobantePagoPorId(rs, new HashMap<>());
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar comprobante por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return comprobante;
    }

    @SuppressWarnings("unchecked")
    private <T> T getOrCreate(Map<Class<?>, Map<Integer, Object>> cache, Class<T> type, int id, Supplier<T> factory) {
        if (id <= 0) return null;
        return (T) cache.computeIfAbsent(type, k -> new HashMap<>())
                .computeIfAbsent(id, k -> factory.get());
    }

    private ComprobantePago mapearComprobantePagoPorId(ResultSet rs, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt("id_comprobante");
        ComprobantePago comprobante = getOrCreate(cache, ComprobantePago.class, id, () -> new ComprobantePago());
        comprobante.setIdComprobante(id);
        comprobante.setTipoDocumento(TipoComprobante.valueOf(rs.getString("tipo_documento")));
        comprobante.setRUCProveedor(rs.getString("ruc_proveedor"));
        comprobante.setRazonSocial(rs.getString("razon_social"));
        comprobante.setNumeroSerial(rs.getString("numero_serie"));
        comprobante.setFechaEmision(rs.getDate("fecha_emision"));
        comprobante.setSubtotal(rs.getDouble("monto_subtotal"));
        comprobante.setIgv(rs.getDouble("monto_igv"));
        comprobante.setMontoTotal(rs.getDouble("monto_total"));
        comprobante.setTipoCambio(rs.getDouble("tipo_cambio"));
        comprobante.setMontoConvertido(rs.getDouble("monto_convertido"));
        comprobante.setNombreArchivoComprobante(rs.getString("nombre_archivo_comprobante"));
        comprobante.setEstado(EstadoComprobante.valueOf(rs.getString("estado_comprobante")));

        SolicitudGasto sg = mapearSolicitudGastoStub(rs, "sg_", cache);
        if (sg != null) {
            if (sg.getComprobantes() == null) sg.setComprobantes(new ArrayList<>());
            sg.getComprobantes().add(comprobante);
            comprobante.setSolicitud(sg);
        }

        Moneda moneda = mapearMoneda(rs, "mon_id_moneda", "mon_", cache);
        if (moneda != null) {
            comprobante.setMoneda(moneda);
        }

        return comprobante;
    }

    private SolicitudGasto mapearSolicitudGastoStub(ResultSet rs, String prefijo, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt(prefijo + "id_solicitud_gasto");
        if (rs.wasNull() || id <= 0) return null;
        SolicitudGasto sg = getOrCreate(cache, SolicitudGasto.class, id, () -> new SolicitudGasto());
        sg.setIdSolicitudGasto(id);
        sg.setFechaSolicitud(rs.getDate(prefijo + "fecha_solicitud"));
        sg.setMontoSolicitado(rs.getDouble(prefijo + "monto_solicitado"));

        int idMoneda = rs.getInt(prefijo + "id_moneda_original");
        if (!rs.wasNull() && idMoneda > 0) {
            Moneda moneda = getOrCreate(cache, Moneda.class, idMoneda, () -> new Moneda());
            moneda.setIdMoneda(idMoneda);
            sg.setMonedaOriginal(moneda);
        }

        sg.setTipoCambio(rs.getDouble(prefijo + "tipo_cambio"));
        sg.setMontoConvertido(rs.getDouble(prefijo + "monto_convertido"));
        sg.setMotivoSolicitud(rs.getString(prefijo + "motivo_solicitud"));
        String sgEstado = rs.getString(prefijo + "estado_solicitud");
        if(sgEstado != null)
            sg.setEstado(EstadoSolicitudGasto.valueOf(sgEstado));
        sg.setComentarioDecision(rs.getString(prefijo + "comentario_decision"));
        int sgIdTransaccion = rs.getInt(prefijo + "id_transaccion");
        if(!rs.wasNull())
            sg.setIdTransaccion(sgIdTransaccion);

        Empleado solicitante = crearEmpleadoStub(rs, prefijo + "id_usuario_solicitante", cache);
        sg.setSolicitante(solicitante);
        Empleado destinatario = crearEmpleadoStub(rs, prefijo + "id_usuario_destinatario", cache);
        sg.setDestinatario(destinatario);
        Empleado jefe = crearEmpleadoStub(rs, prefijo + "id_jefe_aprobador", cache);
        sg.setJefeAprobador(jefe);
        Empleado tesorero = crearEmpleadoStub(rs, prefijo + "id_tesorero_aprobador", cache);
        sg.setTesoreroAprobador(tesorero);

        int idCiclo = rs.getInt(prefijo + "id_ciclo_caja");
        if (!rs.wasNull() && idCiclo > 0) {
            CicloCajaChica ciclo = getOrCreate(cache, CicloCajaChica.class, idCiclo, () -> new CicloCajaChica());
            ciclo.setIdCicloCaja(idCiclo);
            sg.setCiclo(ciclo);
        }

        return sg;
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

    @Override
    public List<ComprobantePago> listarTodas() throws SQLException {
        List<ComprobantePago> comprobantes=null;
        ComprobantePago comprobante;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_comprobantes_pago", null);
        try{
            while(rs.next()){
                if(comprobantes == null) comprobantes = new ArrayList<>();
                comprobante = mapearComprobantePagoPorId(rs, new HashMap<>());
                comprobantes.add(comprobante);
            }
        }catch(SQLException ex){
            System.out.println("Error al listar comprobantes: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return comprobantes;
    }

    @Override
    public List<ComprobantePago> listarActivas() throws SQLException {
        List<ComprobantePago> comprobantes=null;
        ComprobantePago comprobante;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_comprobantes_activos", null);
        try{
            while(rs.next()){
                if(comprobantes == null) comprobantes = new ArrayList<>();
                comprobante = mapearComprobantePagoPorId(rs, new HashMap<>());
                comprobantes.add(comprobante);
            }
        }catch(SQLException ex){
            System.out.println("Error al listar comprobantes activos: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return comprobantes;
    }

    public List<ComprobantePago> listarPorSolicitud(int idSolicitud) throws SQLException {
        List<ComprobantePago> comprobantes=null;
        ComprobantePago comprobante;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_solicitud", idSolicitud);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_comprobantes_por_solicitud", parametrosEntrada);
        try{
            while(rs.next()){
                if(comprobantes == null) comprobantes = new ArrayList<>();
                comprobante = mapearComprobantePagoPorId(rs, new HashMap<>());
                comprobantes.add(comprobante);
            }
        }catch(SQLException ex){
            System.out.println("Error al listar comprobantes por solicitud: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return comprobantes;
    }

}
