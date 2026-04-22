package pe.edu.pucp.economix.operaciones.implement;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.operaciones.dao.ISolicitudGastoDAO;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.EstadoSolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.rrhh.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SolicitudGastoImplement implements ISolicitudGastoDAO {
    private Connection con;
    private Statement st;
    private PreparedStatement pst;
    private ResultSet rs;
    private CallableStatement cs;

    @Override
    public int insertar(SolicitudGasto solicitudGasto) {
        int id = 0;
        try {
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_insertar_solicitud_gasto(?,?,?,?,?,?,?,?)}");

            if (solicitudGasto.getFechaSolicitud() != null)
                cs.setDate("p_fecha_solicitud", new java.sql.Date(solicitudGasto.getFechaSolicitud().getTime()));
            else
                cs.setNull("p_fecha_solicitud", Types.DATE);

            cs.setDouble("p_monto_solicitado", solicitudGasto.getMontoSolicitado());
            cs.setString("p_motivo_solicitud", solicitudGasto.getMotivoSolicitud());
            cs.setString("p_estado_solicitud", solicitudGasto.getEstado().toString());

            if (solicitudGasto.getSolicitante() != null)
                cs.setInt("p_id_usuario_solicitante", solicitudGasto.getSolicitante().getUsuarioID());
            else
                cs.setNull("p_id_usuario_solicitante", Types.INTEGER);

            if (solicitudGasto.getDestinatario() != null)
                cs.setInt("p_id_usuario_destinatario", solicitudGasto.getDestinatario().getUsuarioID());
            else
                cs.setNull("p_id_usuario_destinatario", Types.INTEGER);

            if (solicitudGasto.getCiclo() != null)
                cs.setInt("p_id_ciclo_caja", solicitudGasto.getCiclo().getIdCicloCaja());
            else
                cs.setNull("p_id_ciclo_caja", Types.INTEGER);

            cs.registerOutParameter("p_id_generado", java.sql.Types.INTEGER);

            cs.executeUpdate();

            id = cs.getInt("p_id_generado");
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try {
                cs.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
            try {
                con.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }
        return id;
    }

    @Override
    public int modificar(SolicitudGasto solicitudGasto) {
        int cantidad = 0;

        try {
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_modificar_solicitud_gasto(?,?,?,?,?,?,?,?)}");

            cs.setInt("p_id_solicitud_gasto", solicitudGasto.getIdSolicitudGasto());

            if (solicitudGasto.getFechaSolicitud() != null)
                cs.setDate("p_fecha_solicitud", new java.sql.Date(solicitudGasto.getFechaSolicitud().getTime()));
            else
                cs.setNull("p_fecha_solicitud", Types.DATE);

            cs.setDouble("p_monto_solicitado", solicitudGasto.getMontoSolicitado());
            cs.setString("p_motivo_solicitud", solicitudGasto.getMotivoSolicitud());
            cs.setString("p_estado_solicitud", solicitudGasto.getEstado().toString());

            if (solicitudGasto.getSolicitante() != null)
                cs.setInt("p_id_usuario_solicitante", solicitudGasto.getSolicitante().getUsuarioID());
            else
                cs.setNull("p_id_usuario_solicitante", Types.INTEGER);

            if (solicitudGasto.getDestinatario() != null)
                cs.setInt("p_id_usuario_destinatario", solicitudGasto.getDestinatario().getUsuarioID());
            else
                cs.setNull("p_id_usuario_destinatario", Types.INTEGER);

            if (solicitudGasto.getCiclo() != null)
                cs.setInt("p_id_ciclo_caja", solicitudGasto.getCiclo().getIdCicloCaja());
            else
                cs.setNull("p_id_ciclo_caja", Types.INTEGER);

            cantidad = cs.executeUpdate();

            return cantidad;
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try {
                cs.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
            try {
                con.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }

        return cantidad;
    }

    @Override
    public int eliminar(int idSolicitudGasto) {
        int cantidad = 0;

        try {
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_eliminar_solicitud_gasto(?)}");
            cs.setInt("p_id_solicitud_gasto", idSolicitudGasto);

            cantidad = cs.executeUpdate();

            return cantidad;
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try {
                cs.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
            try {
                con.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }

        return cantidad;
    }

    @Override
    public SolicitudGasto buscarPorId(int idSolicitudGasto) {
        SolicitudGasto solicitud = null;

        try {
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_buscar_solicitud_gasto_por_id(?)}");
            cs.setInt("p_id_solicitud_gasto", idSolicitudGasto);

            rs = cs.executeQuery();
            if (rs.next()) {
                solicitud = new SolicitudGasto();
                solicitud.setIdSolicitudGasto(rs.getInt("id_solicitud_gasto"));
                solicitud.setFechaSolicitud(rs.getDate("fecha_solicitud"));
                solicitud.setMontoSolicitado(rs.getDouble("monto_solicitado"));
                solicitud.setMotivoSolicitud(rs.getString("motivo_solicitud"));
                solicitud.setEstado(EstadoSolicitudGasto.valueOf(rs.getString("estado_solicitud")));

                if (rs.getObject("id_usuario_solicitante") != null) {
                    Empleado usuarioSolicitante = new Empleado();
                    usuarioSolicitante.setUsuarioID(rs.getInt("id_usuario_solicitante"));
                    solicitud.setSolicitante(usuarioSolicitante);
                }

                if (rs.getObject("id_usuario_destinatario") != null) {
                    Empleado usuarioDestinatario = new Empleado();
                    usuarioDestinatario.setUsuarioID(rs.getInt("id_usuario_destinatario"));
                    solicitud.setDestinatario(usuarioDestinatario);
                }

                if (rs.getObject("id_ciclo_caja") != null) {
                    CicloCajaChica cicloCaja = new CicloCajaChica();
                    cicloCaja.setIdCicloCaja(rs.getInt("id_ciclo_caja"));
                    solicitud.setCiclo(cicloCaja);
                }
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try {
                cs.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
            try {
                con.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }

        return solicitud;
    }

    @Override
    public List<SolicitudGasto> listarTodas() {
        List<SolicitudGasto> solicitudes = null;

        try {
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_listar_solicitudes_gasto()}");

            rs = cs.executeQuery();
            while (rs.next()) {
                if (solicitudes == null)
                    solicitudes = new ArrayList<>();

                SolicitudGasto solicitud = new SolicitudGasto();
                solicitud.setIdSolicitudGasto(rs.getInt("id_solicitud_gasto"));
                solicitud.setFechaSolicitud(rs.getDate("fecha_solicitud"));
                solicitud.setMontoSolicitado(rs.getDouble("monto_solicitado"));
                solicitud.setMotivoSolicitud(rs.getString("motivo_solicitud"));
                solicitud.setEstado(EstadoSolicitudGasto.valueOf(rs.getString("estado_solicitud")));

                if (rs.getObject("id_usuario_solicitante") != null) {
                    Empleado usuarioSolicitante = new Empleado();
                    usuarioSolicitante.setUsuarioID(rs.getInt("id_usuario_solicitante"));
                    solicitud.setSolicitante(usuarioSolicitante);
                }

                if (rs.getObject("id_usuario_destinatario") != null) {
                    Empleado usuarioDestinatario = new Empleado();
                    usuarioDestinatario.setUsuarioID(rs.getInt("id_usuario_destinatario"));
                    solicitud.setDestinatario(usuarioDestinatario);
                }

                if (rs.getObject("id_ciclo_caja") != null) {
                    CicloCajaChica cicloCaja = new CicloCajaChica();
                    cicloCaja.setIdCicloCaja(rs.getInt("id_ciclo_caja"));
                    solicitud.setCiclo(cicloCaja);
                }

                solicitudes.add(solicitud);
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try {
                cs.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
            try {
                con.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }

        return solicitudes;
    }

    @Override
    public List<SolicitudGasto> listarPorSolicitante(int idUsuarioSolicitante) {
        List<SolicitudGasto> solicitudes = null;

        try {
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_listar_solicitudes_por_solicitante(?)}");
            cs.setInt("p_id_usuario_solicitante", idUsuarioSolicitante);

            rs = cs.executeQuery();
            while (rs.next()) {
                if (solicitudes == null)
                    solicitudes = new ArrayList<>();

                SolicitudGasto solicitud = new SolicitudGasto();
                solicitud.setIdSolicitudGasto(rs.getInt("id_solicitud_gasto"));
                solicitud.setFechaSolicitud(rs.getDate("fecha_solicitud"));
                solicitud.setMontoSolicitado(rs.getDouble("monto_solicitado"));
                solicitud.setMotivoSolicitud(rs.getString("motivo_solicitud"));
                solicitud.setEstado(EstadoSolicitudGasto.valueOf(rs.getString("estado_solicitud")));

                if (rs.getObject("id_usuario_solicitante") != null) {
                    Empleado usuarioSolicitante = new Empleado();
                    usuarioSolicitante.setUsuarioID(rs.getInt("id_usuario_solicitante"));
                    solicitud.setSolicitante(usuarioSolicitante);
                }

                if (rs.getObject("id_usuario_destinatario") != null) {
                    Empleado usuarioDestinatario = new Empleado();
                    usuarioDestinatario.setUsuarioID(rs.getInt("id_usuario_destinatario"));
                    solicitud.setDestinatario(usuarioDestinatario);
                }

                if (rs.getObject("id_ciclo_caja") != null) {
                    CicloCajaChica cicloCaja = new CicloCajaChica();
                    cicloCaja.setIdCicloCaja(rs.getInt("id_ciclo_caja"));
                    solicitud.setCiclo(cicloCaja);
                }

                solicitudes.add(solicitud);
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try {
                cs.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
            try {
                con.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }

        return solicitudes;
    }

    @Override
    public List<SolicitudGasto> listarPendientesJefe(int idUsuarioDestinatario) {
        List<SolicitudGasto> solicitudes = null;

        try {
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_listar_solicitudes_pendientes_jefe(?)}");
            cs.setInt("p_id_usuario_destinatario", idUsuarioDestinatario);

            rs = cs.executeQuery();
            while (rs.next()) {
                if (solicitudes == null)
                    solicitudes = new ArrayList<>();

                SolicitudGasto solicitud = new SolicitudGasto();
                solicitud.setIdSolicitudGasto(rs.getInt("id_solicitud_gasto"));
                solicitud.setFechaSolicitud(rs.getDate("fecha_solicitud"));
                solicitud.setMontoSolicitado(rs.getDouble("monto_solicitado"));
                solicitud.setMotivoSolicitud(rs.getString("motivo_solicitud"));
                solicitud.setEstado(EstadoSolicitudGasto.valueOf(rs.getString("estado_solicitud")));

                if (rs.getObject("id_usuario_solicitante") != null) {
                    Empleado usuarioSolicitante = new Empleado();
                    usuarioSolicitante.setUsuarioID(rs.getInt("id_usuario_solicitante"));
                    solicitud.setSolicitante(usuarioSolicitante);
                }

                if (rs.getObject("id_usuario_destinatario") != null) {
                    Empleado usuarioDestinatario = new Empleado();
                    usuarioDestinatario.setUsuarioID(rs.getInt("id_usuario_destinatario"));
                    solicitud.setDestinatario(usuarioDestinatario);
                }

                if (rs.getObject("id_ciclo_caja") != null) {
                    CicloCajaChica cicloCaja = new CicloCajaChica();
                    cicloCaja.setIdCicloCaja(rs.getInt("id_ciclo_caja"));
                    solicitud.setCiclo(cicloCaja);
                }

                solicitudes.add(solicitud);
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try {
                cs.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
            try {
                con.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }

        return solicitudes;
    }
}