package pe.edu.pucp.economix.operaciones.implement;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.operaciones.dao.ITransaccionDAO;
import pe.edu.pucp.economix.operaciones.model.MedioPago;
import pe.edu.pucp.economix.operaciones.model.TipoTransaccion;
import pe.edu.pucp.economix.operaciones.model.Transaccion;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaccionImplement implements ITransaccionDAO {
    private Connection con;
    private ResultSet rs;
    private CallableStatement cs;

    @Override
    public int insertar(Transaccion transaccion) {
        int id = 0;
        try {
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_insertar_transaccion(?,?,?,?,?,?,?,?,?,?)}");

            cs.setString("p_tipo_operacion", transaccion.getTipoTransaccion().name());

            if (transaccion.getFecha() != null)
                cs.setTimestamp("p_momento_operacion", new java.sql.Timestamp(transaccion.getFecha().getTime()));
            else
                cs.setNull("p_momento_operacion", Types.TIMESTAMP);

            cs.setDouble("p_monto_transaccion", transaccion.getMonto());
            cs.setString("p_numero_operacion_bancaria", transaccion.getNumeroOperacionBancaria());
            cs.setString("p_medio_pago", transaccion.getMedioPago().name());
            cs.setDouble("p_valor_tipo_cambio", transaccion.getTipoCambio());

            if (transaccion.getCuentaOrigen() != null)
                cs.setInt("p_id_cuenta_origen", transaccion.getCuentaOrigen().getIdCuenta());
            else
                cs.setNull("p_id_cuenta_origen", Types.INTEGER);

            if (transaccion.getCuentaDestino() != null)
                cs.setInt("p_id_cuenta_destino", transaccion.getCuentaDestino().getIdCuenta());
            else
                cs.setNull("p_id_cuenta_destino", Types.INTEGER);

            if (transaccion.getMoneda() != null)
                cs.setInt("p_id_moneda", transaccion.getMoneda().getIdMoneda());
            else
                cs.setNull("p_id_moneda", Types.INTEGER);

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
    public int modificar(Transaccion transaccion) {
        int cantidad = 0;

        try {
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_modificar_transaccion(?,?,?,?,?,?,?,?,?,?)}");

            cs.setInt("p_id_transaccion", transaccion.getIdTransaccion());
            cs.setString("p_tipo_operacion", transaccion.getTipoTransaccion().name());

            if (transaccion.getFecha() != null)
                cs.setTimestamp("p_momento_operacion", new java.sql.Timestamp(transaccion.getFecha().getTime()));
            else
                cs.setNull("p_momento_operacion", Types.TIMESTAMP);

            cs.setDouble("p_monto_transaccion", transaccion.getMonto());
            cs.setString("p_numero_operacion_bancaria", transaccion.getNumeroOperacionBancaria());
            cs.setString("p_medio_pago", transaccion.getMedioPago().name());
            cs.setDouble("p_valor_tipo_cambio", transaccion.getTipoCambio());

            if (transaccion.getCuentaOrigen() != null)
                cs.setInt("p_id_cuenta_origen", transaccion.getCuentaOrigen().getIdCuenta());
            else
                cs.setNull("p_id_cuenta_origen", Types.INTEGER);

            if (transaccion.getCuentaDestino() != null)
                cs.setInt("p_id_cuenta_destino", transaccion.getCuentaDestino().getIdCuenta());
            else
                cs.setNull("p_id_cuenta_destino", Types.INTEGER);

            if (transaccion.getMoneda() != null)
                cs.setInt("p_id_moneda", transaccion.getMoneda().getIdMoneda());
            else
                cs.setNull("p_id_moneda", Types.INTEGER);

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
    public int eliminar(int idTransaccion) {
        int cantidad = 0;

        try {
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_eliminar_transaccion(?)}");
            cs.setInt("p_id_transaccion", idTransaccion);

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
    public Transaccion buscarPorId(int idTransaccion) {
        Transaccion transaccion = null;

        try {
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_buscar_transaccion_por_id(?)}");
            cs.setInt("p_id_transaccion", idTransaccion);

            rs = cs.executeQuery();
            if (rs.next()) {
                transaccion = new Transaccion();
                transaccion.setIdTransaccion(rs.getInt("id_transaccion"));
                transaccion.setTipoTransaccion(TipoTransaccion.valueOf(rs.getString("tipo_operacion")));
                transaccion.setFecha(rs.getTimestamp("momento_operacion"));
                transaccion.setMonto(rs.getDouble("monto_transaccion"));
                transaccion.setNumeroOperacionBancaria(rs.getString("numero_operacion_bancaria"));
                transaccion.setMedioPago(MedioPago.valueOf(rs.getString("medio_pago")));
                transaccion.setTipoCambio(rs.getDouble("valor_tipo_cambio"));

                if (rs.getObject("id_cuenta_origen") != null) {
                    CuentaBancaria cuentaOrigen = new CuentaBancaria();
                    cuentaOrigen.setIdCuenta(rs.getInt("id_cuenta_origen"));
                    transaccion.setCuentaOrigen(cuentaOrigen);
                }

                if (rs.getObject("id_cuenta_destino") != null) {
                    CuentaBancaria cuentaDestino = new CuentaBancaria();
                    cuentaDestino.setIdCuenta(rs.getInt("id_cuenta_destino"));
                    transaccion.setCuentaDestino(cuentaDestino);
                }

                if (rs.getObject("id_moneda") != null) {
                    Moneda moneda = new Moneda();
                    moneda.setIdMoneda(rs.getInt("id_moneda"));
                    transaccion.setMoneda(moneda);
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

        return transaccion;
    }

    @Override
    public List<Transaccion> listarTodas() {
        List<Transaccion> transacciones = null;

        try {
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_listar_transacciones()}");

            rs = cs.executeQuery();
            while (rs.next()) {
                if (transacciones == null)
                    transacciones = new ArrayList<>();

                Transaccion transaccion = new Transaccion();
                transaccion.setIdTransaccion(rs.getInt("id_transaccion"));
                transaccion.setTipoTransaccion(TipoTransaccion.valueOf(rs.getString("tipo_operacion")));
                transaccion.setFecha(rs.getTimestamp("momento_operacion"));
                transaccion.setMonto(rs.getDouble("monto_transaccion"));
                transaccion.setNumeroOperacionBancaria(rs.getString("numero_operacion_bancaria"));
                transaccion.setMedioPago(MedioPago.valueOf(rs.getString("medio_pago")));
                transaccion.setTipoCambio(rs.getDouble("valor_tipo_cambio"));

                if (rs.getObject("id_cuenta_origen") != null) {
                    CuentaBancaria cuentaOrigen = new CuentaBancaria();
                    cuentaOrigen.setIdCuenta(rs.getInt("id_cuenta_origen"));
                    transaccion.setCuentaOrigen(cuentaOrigen);
                }

                if (rs.getObject("id_cuenta_destino") != null) {
                    CuentaBancaria cuentaDestino = new CuentaBancaria();
                    cuentaDestino.setIdCuenta(rs.getInt("id_cuenta_destino"));
                    transaccion.setCuentaDestino(cuentaDestino);
                }

                if (rs.getObject("id_moneda") != null) {
                    Moneda moneda = new Moneda();
                    moneda.setIdMoneda(rs.getInt("id_moneda"));
                    transaccion.setMoneda(moneda);
                }

                transacciones.add(transaccion);
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

        return transacciones;
    }
}