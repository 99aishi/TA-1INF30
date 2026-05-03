package pe.edu.pucp.economix.operaciones.implement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.operaciones.dao.ISolicitudGastoDAO;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.EstadoSolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.rrhh.model.Empleado;

public class SolicitudGastoImplement implements ISolicitudGastoDAO {
    private ResultSet rs;

    @Override
    public int insertar(SolicitudGasto solicitudGasto) throws SQLException{
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_generado", Types.INTEGER);
        parametrosEntrada.put("p_fecha_solicitud", solicitudGasto.getFechaSolicitud());
        parametrosEntrada.put("p_monto_solicitado", solicitudGasto.getMontoSolicitado());
        parametrosEntrada.put("p_motivo_solicitud", solicitudGasto.getMotivoSolicitud());
        parametrosEntrada.put("p_estado_solicitud", solicitudGasto.getEstado().toString());
        if(solicitudGasto.getSolicitante() != null)
            parametrosEntrada.put("p_id_usuario_solicitante", solicitudGasto.getSolicitante().getUsuarioID());
        else
            parametrosEntrada.put("p_id_usuario_solicitante", null);
        if(solicitudGasto.getDestinatario() != null)
            parametrosEntrada.put("p_id_usuario_destinatario", solicitudGasto.getDestinatario().getUsuarioID());
        else
            parametrosEntrada.put("p_id_usuario_destinatario", null);
        if(solicitudGasto.getCiclo() != null)
            parametrosEntrada.put("p_id_ciclo_caja", solicitudGasto.getCiclo().getIdCicloCaja());
        else
            parametrosEntrada.put("p_id_ciclo_caja", null);

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_solicitud_gasto", parametrosEntrada, parametrosSalida);
        solicitudGasto.setIdSolicitudGasto((int)parametrosSalida.get("p_id_generado"));

        return solicitudGasto.getIdSolicitudGasto();
    }

    @Override
    public int modificar(SolicitudGasto solicitudGasto) throws SQLException {
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_generado", Types.INTEGER);
        parametrosEntrada.put("p_fecha_solicitud", solicitudGasto.getFechaSolicitud());
        parametrosEntrada.put("p_monto_solicitado", solicitudGasto.getMontoSolicitado());
        parametrosEntrada.put("p_motivo_solicitud", solicitudGasto.getMotivoSolicitud());
        parametrosEntrada.put("p_estado_solicitud", solicitudGasto.getEstado().toString());
        if(solicitudGasto.getSolicitante() != null)
            parametrosEntrada.put("p_id_usuario_solicitante", solicitudGasto.getSolicitante().getUsuarioID());
        else
            parametrosEntrada.put("p_id_usuario_solicitante", null);
        if(solicitudGasto.getDestinatario() != null)
            parametrosEntrada.put("p_id_usuario_destinatario", solicitudGasto.getDestinatario().getUsuarioID());
        else
            parametrosEntrada.put("p_id_usuario_destinatario", null);
        if(solicitudGasto.getCiclo() != null)
            parametrosEntrada.put("p_id_ciclo_caja", solicitudGasto.getCiclo().getIdCicloCaja());
        else
            parametrosEntrada.put("p_id_ciclo_caja", null);

        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_solicitud_gasto", parametrosEntrada, null);

        return resultado;
    }

    @Override
    public int eliminar(int idSolicitudGasto) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
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
                solicitud = new SolicitudGasto();
                solicitud.setIdSolicitudGasto(rs.getInt("id_solicitud_gasto"));
                solicitud.setFechaSolicitud(rs.getDate("fecha_solicitud"));
                solicitud.setMontoSolicitado(rs.getDouble("monto_solicitado"));
                solicitud.setMotivoSolicitud(rs.getString("motivo_solicitud"));
                solicitud.setEstado(EstadoSolicitudGasto.valueOf(rs.getString("estado_solicitud")));
                if(solicitud.getSolicitante() == null)
                    solicitud.setSolicitante(new Empleado());
                solicitud.getSolicitante().setUsuarioID(rs.getInt("id_usuario_solicitante"));
                if(solicitud.getDestinatario() == null)
                    solicitud.setDestinatario(new Empleado());
                solicitud.getDestinatario().setUsuarioID(rs.getInt("id_usuario_destinatario"));
                if(solicitud.getCiclo() == null)
                    solicitud.setCiclo(new CicloCajaChica());
                solicitud.getCiclo().setIdCicloCaja(rs.getInt("id_ciclo_caja"));
                
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar caja chica por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return solicitud;
    }

    @Override
    public List<SolicitudGasto> listarTodas() throws SQLException {
        List<SolicitudGasto> solicitudes = null;
        SolicitudGasto solicitud;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_todas_solicitudes_gasto", null);
        try{
            while(rs.next()){
                if(solicitudes == null) solicitudes = new ArrayList<>();
                solicitud = new SolicitudGasto();
                solicitud.setIdSolicitudGasto(rs.getInt("id_solicitud_gasto"));
                solicitud.setFechaSolicitud(rs.getDate("fecha_solicitud"));
                solicitud.setMontoSolicitado(rs.getDouble("monto_solicitado"));
                solicitud.setMotivoSolicitud(rs.getString("motivo_solicitud"));
                solicitud.setEstado(EstadoSolicitudGasto.valueOf(rs.getString("estado_solicitud")));
                if(solicitud.getSolicitante() == null)
                    solicitud.setSolicitante(new Empleado());
                solicitud.getSolicitante().setUsuarioID(rs.getInt("id_usuario_solicitante"));
                if(solicitud.getDestinatario() == null)
                    solicitud.setDestinatario(new Empleado());
                solicitud.getDestinatario().setUsuarioID(rs.getInt("id_usuario_destinatario"));
                if(solicitud.getCiclo() == null)
                    solicitud.setCiclo(new CicloCajaChica());
                solicitud.getCiclo().setIdCicloCaja(rs.getInt("id_ciclo_caja"));
                solicitudes.add(solicitud);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar caja chica por id: " + ex.getMessage());
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
                solicitud = new SolicitudGasto();
                solicitud.setIdSolicitudGasto(rs.getInt("id_solicitud_gasto"));
                solicitud.setFechaSolicitud(rs.getDate("fecha_solicitud"));
                solicitud.setMontoSolicitado(rs.getDouble("monto_solicitado"));
                solicitud.setMotivoSolicitud(rs.getString("motivo_solicitud"));
                solicitud.setEstado(EstadoSolicitudGasto.valueOf(rs.getString("estado_solicitud")));
                if(solicitud.getSolicitante() == null)
                    solicitud.setSolicitante(new Empleado());
                solicitud.getSolicitante().setUsuarioID(rs.getInt("id_usuario_solicitante"));
                if(solicitud.getDestinatario() == null)
                    solicitud.setDestinatario(new Empleado());
                solicitud.getDestinatario().setUsuarioID(rs.getInt("id_usuario_destinatario"));
                if(solicitud.getCiclo() == null)
                    solicitud.setCiclo(new CicloCajaChica());
                solicitud.getCiclo().setIdCicloCaja(rs.getInt("id_ciclo_caja"));
                solicitudes.add(solicitud);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar caja chica por id: " + ex.getMessage());
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
                solicitud = new SolicitudGasto();
                solicitud.setIdSolicitudGasto(rs.getInt("id_solicitud_gasto"));
                solicitud.setFechaSolicitud(rs.getDate("fecha_solicitud"));
                solicitud.setMontoSolicitado(rs.getDouble("monto_solicitado"));
                solicitud.setMotivoSolicitud(rs.getString("motivo_solicitud"));
                solicitud.setEstado(EstadoSolicitudGasto.valueOf(rs.getString("estado_solicitud")));
                if(solicitud.getSolicitante() == null)
                    solicitud.setSolicitante(new Empleado());
                solicitud.getSolicitante().setUsuarioID(rs.getInt("id_usuario_solicitante"));
                if(solicitud.getDestinatario() == null)
                    solicitud.setDestinatario(new Empleado());
                solicitud.getDestinatario().setUsuarioID(rs.getInt("id_usuario_destinatario"));
                if(solicitud.getCiclo() == null)
                    solicitud.setCiclo(new CicloCajaChica());
                solicitud.getCiclo().setIdCicloCaja(rs.getInt("id_ciclo_caja"));
                solicitudes.add(solicitud);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar caja chica por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return solicitudes;
    }
}