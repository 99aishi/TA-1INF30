package pe.edu.pucp.economix.operaciones.implement;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.dao.IDAO;
import pe.edu.pucp.economix.operaciones.dao.IComprobantePagoDAO;
import pe.edu.pucp.economix.operaciones.model.ComprobantePago;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.TipoComprobante;
import pe.edu.pucp.economix.tesoreria.model.Fondo;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComprobantePagoImplement implements IComprobantePagoDAO {
    private Connection con;
    private CallableStatement cs;
    private ResultSet rs;

    @Override
    public int insertar(ComprobantePago comprobante) {
        int id = 0;
        try {
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_insertar_comprobante_pago(?,?,?,?,?,?,?,?,?,?,?,?)}");

            cs.setString("p_tipo_documento", comprobante.getTipoDocumento().toString());
            cs.setString("p_ruc_proveedor", comprobante.getRuc());
            cs.setString("p_razon_social", comprobante.getRazonSocial());
            cs.setString("p_numero_serie", comprobante.getNumeroSerial());

            if (comprobante.getFechaEmision() != null)
                cs.setDate("p_fecha_emision", new java.sql.Date(comprobante.getFechaEmision().getTime()));
            else
                cs.setNull("p_fecha_emision", Types.DATE);

            cs.setDouble("p_monto_subtotal", comprobante.getSubtotal());
            cs.setDouble("p_monto_igv", comprobante.getIgv());
            cs.setDouble("p_monto_total", comprobante.getMontoTotal());

            // Relación con Solicitud de Gasto
            if (comprobante.getSolicitud() != null)
                cs.setInt("p_id_solicitud_gasto", comprobante.getSolicitud().getIdSolicitudGasto());
            else
                cs.setNull("p_id_solicitud_gasto", Types.INTEGER);

            if (comprobante.getFondoEntrega() != null)
                cs.setInt("p_id_fondo_entrega", comprobante.getFondoEntrega().getIdFondo());
            else
                cs.setNull("p_id_fondo_entrega", Types.INTEGER);

            if (comprobante.getMoneda() != null)
                cs.setInt("p_id_moneda", comprobante.getMoneda().getIdMoneda());
            else
                cs.setNull("p_id_moneda", Types.INTEGER);

            cs.registerOutParameter("p_id_generado", java.sql.Types.INTEGER);

            cs.executeUpdate();

            id = cs.getInt("p_id_generado");
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { if (cs != null) cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (con != null) con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return id;
    }

    @Override
    public int modificar(ComprobantePago comprobante) {
        int cantidad = 0;
        try {
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_modificar_comprobante_pago(?,?,?,?,?,?,?,?,?,?,?,?)}");

            cs.setInt("p_id_comprobante", comprobante.getIdComprobante());
            cs.setString("p_tipo_documento", comprobante.getTipoDocumento().toString());
            cs.setString("p_ruc_proveedor", comprobante.getRuc());
            cs.setString("p_razon_social", comprobante.getRazonSocial());
            cs.setString("p_numero_serie", comprobante.getNumeroSerial());

            if (comprobante.getFechaEmision() != null)
                cs.setDate("p_fecha_emision", new java.sql.Date(comprobante.getFechaEmision().getTime()));
            else
                cs.setNull("p_fecha_emision", Types.DATE);

            cs.setDouble("p_monto_subtotal", comprobante.getSubtotal());
            cs.setDouble("p_monto_igv", comprobante.getIgv());
            cs.setDouble("p_monto_total", comprobante.getMontoTotal());

            // Relaciones
            if (comprobante.getSolicitud() != null)
                cs.setInt("p_id_solicitud_gasto", comprobante.getSolicitud().getIdSolicitudGasto());
            else
                cs.setNull("p_id_solicitud_gasto", Types.INTEGER);

            if (comprobante.getFondoEntrega() != null)
                cs.setInt("p_id_fondo_entrega", comprobante.getFondoEntrega().getIdFondo());
            else
                cs.setNull("p_id_fondo_entrega", Types.INTEGER);

            /* if (comprobante.getMoneda() != null)
                cs.setInt("p_id_moneda", comprobante.getMoneda().getIdMoneda());
            else
            */
            cs.setNull("p_id_moneda", Types.INTEGER);

            cantidad = cs.executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { if (cs != null) cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (con != null) con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return cantidad;
    }

    @Override
    public int eliminar(int id) {
        int cantidad = 0;
        try {
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_eliminar_comprobante_pago(?)}");
            cs.setInt("p_id_comprobante", id);

            cantidad = cs.executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { if (cs != null) cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (con != null) con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return cantidad;
    }

    @Override
    public ComprobantePago buscarPorId(int id) {
        ComprobantePago comprobante = null;
        try {
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_buscar_comprobante_pago_por_id(?)}");
            cs.setInt("p_id_comprobante", id);

            rs = cs.executeQuery();
            if (rs.next()) {
                comprobante = extraerComprobante(rs);
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (cs != null) cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (con != null) con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return comprobante;
    }

    @Override
    public List<ComprobantePago> listarTodas() {
        List<ComprobantePago> comprobantes = new ArrayList<>();
        try {
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_listar_comprobantes_pago()}");

            rs = cs.executeQuery();
            while (rs.next()) {
                ComprobantePago cp = extraerComprobante(rs);
                comprobantes.add(cp);
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (cs != null) cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (con != null) con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return comprobantes;
    }

    @Override
    public List<ComprobantePago> listarPorSolicitud(int idSolicitud) {
        List<ComprobantePago> comprobantes = new ArrayList<>();
        try {
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_listar_comprobantes_por_solicitud(?)}");
            cs.setInt("p_id_solicitud_gasto", idSolicitud);

            rs = cs.executeQuery();
            while (rs.next()) {
                ComprobantePago cp = extraerComprobante(rs);
                comprobantes.add(cp);
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (cs != null) cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (con != null) con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return comprobantes;
    }

    // Método auxiliar para evitar repetir código en las lecturas
    private ComprobantePago extraerComprobante(ResultSet rs) throws SQLException {
        ComprobantePago cp = new ComprobantePago(); // <-- Requiere constructor vacío
        cp.setIdComprobante(rs.getInt("id_comprobante"));

        if (rs.getString("tipo_documento") != null)
            cp.setTipoDocumento(TipoComprobante.valueOf(rs.getString("tipo_documento")));

        cp.setRuc(rs.getString("ruc_proveedor"));
        cp.setRazonSocial(rs.getString("razon_social"));
        cp.setNumeroSerial(rs.getString("numero_serie"));
        cp.setFechaEmision(rs.getDate("fecha_emision"));
        cp.setSubtotal(rs.getDouble("monto_subtotal"));
        cp.setIgv(rs.getDouble("monto_igv"));
        cp.setMontoTotal(rs.getDouble("monto_total"));

        // Enlazando objeto Solicitud Gasto (solo guardando el ID como pediste)
        if (rs.getObject("id_solicitud_gasto") != null) {
            SolicitudGasto solicitud = new SolicitudGasto();
            solicitud.setIdSolicitudGasto(rs.getInt("id_solicitud_gasto"));
            cp.setSolicitud(solicitud);
        }

        if (rs.getObject("id_fondo_entrega") != null) {
            Fondo fondo = new Fondo();
            fondo.setIdFondo(rs.getInt("id_fondo_entrega"));
            cp.setFondoEntrega(fondo);
        }

        if (rs.getObject("id_moneda") != null) {
            Moneda moneda = new Moneda();
            moneda.setIdMoneda(rs.getInt("id_moneda"));
            cp.setMoneda(moneda);
        }

        return cp;
    }
}