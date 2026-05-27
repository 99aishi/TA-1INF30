package pe.edu.pucp.economix.operaciones.implement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.operaciones.dao.IComprobantePagoDAO;
import pe.edu.pucp.economix.operaciones.model.ComprobantePago;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoComprobante;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.enums.TipoComprobante;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

public class ComprobantePagoImplement implements IComprobantePagoDAO {
    private ResultSet rs;

    @Override
    public int insertar(ComprobantePago comprobante) throws SQLException {
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_generado", Types.INTEGER);
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
    public int modificar(ComprobantePago comprobante) throws SQLException {
        Map<String,Object> parametrosEntrada = new HashMap<>();
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
    public int eliminar(int idComprobante) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_comprobante", idComprobante);
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_eliminar_comprobante_pago", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public ComprobantePago buscarPorId(int idComprobante) throws SQLException {
        ComprobantePago comprobante = null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_ciclo_caja", idComprobante);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_buscar_ciclo_caja_por_id", parametrosEntrada);
        try{
            if(rs.next()){
                comprobante = new ComprobantePago();
                comprobante.setIdComprobante(rs.getInt("id_comprobante"));
                comprobante.setTipoDocumento(TipoComprobante.valueOf(rs.getString("tipo_documento")));
                comprobante.setRUCProveedor(rs.getString("ruc_proveedor"));
                comprobante.setRazonSocial(rs.getString("razon_social"));
                comprobante.setNumeroSerial(rs.getString("numero_serie"));
                comprobante.setFechaEmision(rs.getDate("fecha_emision"));
                comprobante.setSubtotal(rs.getDouble("monto_subtotal"));
                comprobante.setIgv(rs.getDouble("monto_igv"));
                comprobante.setMontoTotal(rs.getDouble("monto_total"));
                comprobante.setEstado(EstadoComprobante.valueOf(rs.getString("estado_comprobante")));
                if(comprobante.getSolicitud() == null)
                    comprobante.setSolicitud(new SolicitudGasto());
                comprobante.getSolicitud().setIdSolicitudGasto(rs.getInt("id_solicitud_gasto"));
                if(comprobante.getMoneda() == null)
                    comprobante.setMoneda(new Moneda());
                comprobante.getMoneda().setIdMoneda(rs.getInt("id_moneda"));    
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar caja chica por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return comprobante;
    }

    @Override
    public List<ComprobantePago> listarTodas() throws SQLException {
        List<ComprobantePago> comprobantes=null;
        ComprobantePago comprobante;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_comprobantes_pago", null);
        try{
            while(rs.next()){
                if(comprobantes == null) comprobantes = new ArrayList<>();
                comprobante = new ComprobantePago();
                comprobante.setIdComprobante(rs.getInt("id_comprobante"));
                comprobante.setTipoDocumento(TipoComprobante.valueOf(rs.getString("tipo_documento")));
                comprobante.setRUCProveedor(rs.getString("ruc_proveedor"));
                comprobante.setRazonSocial(rs.getString("razon_social"));
                comprobante.setNumeroSerial(rs.getString("numero_serie"));
                comprobante.setFechaEmision(rs.getDate("fecha_emision"));
                comprobante.setSubtotal(rs.getDouble("monto_subtotal"));
                comprobante.setIgv(rs.getDouble("monto_igv"));
                comprobante.setMontoTotal(rs.getDouble("monto_total"));
                comprobante.setEstado(EstadoComprobante.valueOf(rs.getString("estado_comprobante")));
                if(comprobante.getSolicitud() == null)
                    comprobante.setSolicitud(new SolicitudGasto());
                comprobante.getSolicitud().setIdSolicitudGasto(rs.getInt("id_solicitud_gasto"));
                if(comprobante.getMoneda() == null)
                    comprobante.setMoneda(new Moneda());
                comprobante.getMoneda().setIdMoneda(rs.getInt("id_moneda"));    
                comprobantes.add(comprobante);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar caja chica por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return comprobantes;
    }
}