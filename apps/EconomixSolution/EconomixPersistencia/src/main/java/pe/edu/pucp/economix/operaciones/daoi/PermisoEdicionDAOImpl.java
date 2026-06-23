package pe.edu.pucp.economix.operaciones.daoi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.operaciones.idao.IPermisoEdicionDAO;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.ComprobantePago;
import pe.edu.pucp.economix.operaciones.model.PermisoEdicion;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoCicloCaja;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoComprobante;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoPermisoEdicion;
import pe.edu.pucp.economix.operaciones.model.enums.TipoComprobante;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;

public class PermisoEdicionDAOImpl implements IPermisoEdicionDAO {
    private ResultSet rs;

    @Override
    public int solicitar(PermisoEdicion permiso, int idUsuarioAccion) throws SQLException {
        Map<String, Object> parametrosSalida = new HashMap<>();
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_generado", Types.INTEGER);
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_usuario_solicitante",
                permiso.getSolicitante() != null ? permiso.getSolicitante().getUsuarioID() : null);
        parametrosEntrada.put("p_id_comprobante",
                permiso.getComprobante() != null ? permiso.getComprobante().getIdComprobante() : null);
        parametrosEntrada.put("p_motivo_solicitud", permiso.getMotivoSolicitud());

        DBManager.getDBManager().ejecutarProcedimiento("pa_solicitar_permiso_edicion", parametrosEntrada, parametrosSalida);
        int idGenerado = (int) parametrosSalida.get("p_id_generado");
        permiso.setIdPermiso(idGenerado);
        return idGenerado;
    }

    @Override
    public int otorgar(int idPermiso, int idAutorizador, String motivoAutorizacion, int idUsuarioAccion) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_permiso", idPermiso);
        parametrosEntrada.put("p_id_usuario_autorizador", idAutorizador);
        parametrosEntrada.put("p_motivo_autorizacion", motivoAutorizacion);
        return DBManager.getDBManager().ejecutarProcedimiento("pa_otorgar_permiso_edicion", parametrosEntrada, null);
    }

    @Override
    public int revocar(int idPermiso, int idUsuarioAccion) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_permiso", idPermiso);
        return DBManager.getDBManager().ejecutarProcedimiento("pa_revocar_permiso_edicion", parametrosEntrada, null);
    }

    @Override
    public List<PermisoEdicion> listarPendientes(int idAutorizador) throws SQLException {
        List<PermisoEdicion> permisos = new ArrayList<>();
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_autorizador", idAutorizador);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_permisos_pendientes", parametrosEntrada);
        try {
            while (rs.next()) {
                PermisoEdicion p = mapearPermiso(rs);
                permisos.add(p);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar permisos pendientes: " + ex.getMessage());
        } finally {
            DBManager.getDBManager().cerrarConexion();
        }
        return permisos;
    }

    @Override
    public List<PermisoEdicion> listarComprobantesEnExcepcion() throws SQLException {
        List<PermisoEdicion> permisos = new ArrayList<>();
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_comprobantes_en_excepcion", null);
        try {
            while (rs.next()) {
                PermisoEdicion p = mapearPermisoExcepcion(rs);
                permisos.add(p);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar comprobantes en excepcion: " + ex.getMessage());
        } finally {
            DBManager.getDBManager().cerrarConexion();
        }
        return permisos;
    }

    private PermisoEdicion mapearPermiso(ResultSet rs) throws SQLException {
        PermisoEdicion p = new PermisoEdicion();
        p.setIdPermiso(rs.getInt("id_permiso"));
        p.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        p.setFechaExpiracion(rs.getTimestamp("fecha_expiracion"));
        p.setFechaUso(rs.getTimestamp("fecha_uso"));
        p.setMotivoSolicitud(rs.getString("motivo_solicitud"));
        p.setMotivoAutorizacion(rs.getString("motivo_autorizacion"));
        String estado = rs.getString("estado");
        if (estado != null) {
            p.setEstado(EstadoPermisoEdicion.valueOf(estado));
        }

        Empleado solicitante = new Empleado();
        solicitante.setUsuarioID(rs.getInt("id_usuario_solicitante"));
        solicitante.setNombres(rs.getString("sol_nombres"));
        solicitante.setApellidoPaterno(rs.getString("sol_apellido_paterno"));
        solicitante.setApellidoMaterno(rs.getString("sol_apellido_materno"));
        p.setSolicitante(solicitante);

        int idAutorizador = rs.getInt("id_usuario_autorizador");
        if (!rs.wasNull() && idAutorizador > 0) {
            Empleado autorizador = new Empleado();
            autorizador.setUsuarioID(idAutorizador);
            p.setAutorizador(autorizador);
        }

        ComprobantePago cp = new ComprobantePago();
        cp.setIdComprobante(rs.getInt("id_comprobante"));
        cp.setTipoDocumento(TipoComprobante.valueOf(rs.getString("cp_tipo_documento")));
        cp.setRUCProveedor(rs.getString("cp_ruc_proveedor"));
        cp.setRazonSocial(rs.getString("cp_razon_social"));
        cp.setMontoTotal(rs.getDouble("cp_monto_total"));
        String cpEstado = rs.getString("cp_estado_comprobante");
        if (cpEstado != null) {
            cp.setEstado(EstadoComprobante.valueOf(cpEstado));
        }

        SolicitudGasto sg = new SolicitudGasto();
        sg.setIdSolicitudGasto(rs.getInt("sg_id_solicitud_gasto"));
        sg.setMontoSolicitado(rs.getDouble("sg_monto_solicitado"));
        cp.setSolicitud(sg);

        int idCiclo = rs.getInt("ciclo_id_ciclo_caja");
        if (!rs.wasNull() && idCiclo > 0) {
            CicloCajaChica ciclo = new CicloCajaChica();
            ciclo.setIdCicloCaja(idCiclo);
            String cicloEstado = rs.getString("ciclo_estado_ciclo");
            if (cicloEstado != null) {
                ciclo.setEstado(EstadoCicloCaja.valueOf(cicloEstado));
            }
            sg.setCiclo(ciclo);
        }

        p.setComprobante(cp);
        return p;
    }

    private PermisoEdicion mapearPermisoExcepcion(ResultSet rs) throws SQLException {
        PermisoEdicion p = new PermisoEdicion();

        ComprobantePago cp = new ComprobantePago();
        cp.setIdComprobante(rs.getInt("id_comprobante"));
        cp.setTipoDocumento(TipoComprobante.valueOf(rs.getString("tipo_documento")));
        cp.setRUCProveedor(rs.getString("ruc_proveedor"));
        cp.setRazonSocial(rs.getString("razon_social"));
        cp.setMontoTotal(rs.getDouble("monto_total"));
        String cpEstado = rs.getString("estado_comprobante");
        if (cpEstado != null) {
            cp.setEstado(EstadoComprobante.valueOf(cpEstado));
        }

        SolicitudGasto sg = new SolicitudGasto();
        sg.setIdSolicitudGasto(rs.getInt("id_solicitud_gasto"));
        sg.setMontoSolicitado(rs.getDouble("sg_monto_solicitado"));

        Empleado solicitante = new Empleado();
        solicitante.setUsuarioID(rs.getInt("sg_id_usuario_solicitante"));
        solicitante.setNombres(rs.getString("sol_nombres"));
        solicitante.setApellidoPaterno(rs.getString("sol_apellido_paterno"));
        solicitante.setApellidoMaterno(rs.getString("sol_apellido_materno"));
        sg.setSolicitante(solicitante);
        cp.setSolicitud(sg);

        CicloCajaChica ciclo = new CicloCajaChica();
        ciclo.setIdCicloCaja(rs.getInt("ciclo_id_ciclo_caja"));
        ciclo.setNumeroSemana(rs.getInt("ciclo_numero_semana"));
        ciclo.setFechaApertura(rs.getDate("ciclo_fecha_apertura"));
        ciclo.setFechaCierre(rs.getDate("ciclo_fecha_cierre"));
        ciclo.setSaldoInicial(rs.getDouble("ciclo_saldo_inicial"));
        ciclo.setEstado(EstadoCicloCaja.EN_EXCEPCION);

        CajaChica cajaChica = new CajaChica();
        cajaChica.setIdFondo(rs.getInt("ccj_id_fondo"));
        cajaChica.setNombre(rs.getString("ccj_nombre_fondo"));
        ciclo.setCajaChica(cajaChica);

        sg.setCiclo(ciclo);
        p.setComprobante(cp);

        return p;
    }
}
