package pe.edu.pucp.economix.operaciones.implement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.operaciones.dao.ITransaccionDAO;
import pe.edu.pucp.economix.operaciones.model.enums.MedioPago;
import pe.edu.pucp.economix.operaciones.model.enums.TipoTransaccion;
import pe.edu.pucp.economix.operaciones.model.Transaccion;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

public class TransaccionImplement implements ITransaccionDAO {
    private ResultSet rs;

    @Override
    public int insertar(Transaccion transaccion) throws SQLException {        
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_generado", Types.INTEGER);
        parametrosEntrada.put("p_tipo_operacion", transaccion.getTipoTransaccion().toString());
        parametrosEntrada.put("p_momento_operacion", transaccion.getFecha());
        parametrosEntrada.put("p_monto_transaccion", transaccion.getMonto());
        parametrosEntrada.put("p_numero_operacion_bancaria", transaccion.getNumeroOperacionBancaria());
        parametrosEntrada.put("p_medio_pago", transaccion.getMedioPago().toString());
        parametrosEntrada.put("p_valor_tipo_cambio", transaccion.getTipoCambio());
        if(transaccion.getCuentaOrigen() != null)
            parametrosEntrada.put("p_id_cuenta_origen", transaccion.getCuentaOrigen().getIdCuenta());
        else
            parametrosEntrada.put("p_id_cuenta_origen", null);
        if(transaccion.getCuentaDestino() != null)
            parametrosEntrada.put("p_id_cuenta_destino", transaccion.getCuentaDestino().getIdCuenta());
        else
            parametrosEntrada.put("p_id_cuenta_destino", null);
        if(transaccion.getMoneda() != null)
            parametrosEntrada.put("p_id_moneda", transaccion.getMoneda().getIdMoneda());
        else
            parametrosEntrada.put("p_id_moneda", null);

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_transaccion", parametrosEntrada, parametrosSalida);
        transaccion.setIdTransaccion((int)parametrosSalida.get("p_id_generado"));

        return transaccion.getIdTransaccion();
    }

    @Override
    public int modificar(Transaccion transaccion) throws SQLException {
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_transaccion", transaccion.getIdTransaccion());
        parametrosEntrada.put("p_tipo_operacion", transaccion.getTipoTransaccion().toString());
        parametrosEntrada.put("p_momento_operacion", transaccion.getFecha());
        parametrosEntrada.put("p_monto_transaccion", transaccion.getMonto());
        parametrosEntrada.put("p_numero_operacion_bancaria", transaccion.getNumeroOperacionBancaria());
        parametrosEntrada.put("p_medio_pago", transaccion.getMedioPago().toString());
        parametrosEntrada.put("p_valor_tipo_cambio", transaccion.getTipoCambio());
        if(transaccion.getCuentaOrigen() != null)
            parametrosEntrada.put("p_id_cuenta_origen", transaccion.getCuentaOrigen().getIdCuenta());
        else
            parametrosEntrada.put("p_id_cuenta_origen", null);
        if(transaccion.getCuentaDestino() != null)
            parametrosEntrada.put("p_id_cuenta_destino", transaccion.getCuentaDestino().getIdCuenta());
        else
            parametrosEntrada.put("p_id_cuenta_destino", null);
        if(transaccion.getMoneda() != null)
            parametrosEntrada.put("p_id_moneda", transaccion.getMoneda().getIdMoneda());
        else
            parametrosEntrada.put("p_id_moneda", null);

        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_transaccion", parametrosEntrada, null);

        return resultado;
    }

    @Override
    public int eliminar(int idTransaccion) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_transaccion", idTransaccion);
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_eliminar_transaccion", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public Transaccion buscarPorId(int idTransaccion) throws SQLException {
        Transaccion transaccion = null;

        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_transaccion", idTransaccion);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_buscar_transaccion_por_id", parametrosEntrada);
        try{
            if(rs.next()){
                transaccion = new Transaccion();
                transaccion.setIdTransaccion(rs.getInt("id_transaccion"));
                transaccion.setTipoTransaccion(TipoTransaccion.valueOf(rs.getString("tipo_operacion")));
                transaccion.setFecha(rs.getTimestamp("momento_operacion"));
                transaccion.setMonto(rs.getDouble("monto_transaccion"));
                transaccion.setNumeroOperacionBancaria(rs.getString("numero_operacion_bancaria"));
                transaccion.setMedioPago(MedioPago.valueOf(rs.getString("medio_pago")));
                transaccion.setTipoCambio(rs.getDouble("valor_tipo_cambio"));
                if(transaccion.getCuentaOrigen() == null)
                    transaccion.setCuentaOrigen(new CuentaBancaria());
                transaccion.getCuentaOrigen().setIdCuenta(rs.getInt("id_cuenta_origen"));
                if(transaccion.getCuentaDestino() == null)
                    transaccion.setCuentaDestino(new CuentaBancaria());
                transaccion.getCuentaDestino().setIdCuenta(rs.getInt("id_cuenta_destino"));
                if(transaccion.getMoneda() == null)
                    transaccion.setMoneda(new Moneda());
                transaccion.getMoneda().setIdMoneda(rs.getInt("id_moneda"));
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar caja chica por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }

        return transaccion;
    }

    @Override
    public List<Transaccion> listarTodas() throws SQLException {
        List<Transaccion> transacciones = null;
        Transaccion transaccion;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_transacciones", null);
        try{
            if(rs.next()){
                if(transacciones == null) transacciones = new ArrayList<>();
                transaccion = new Transaccion();
                transaccion.setIdTransaccion(rs.getInt("id_transaccion"));
                transaccion.setTipoTransaccion(TipoTransaccion.valueOf(rs.getString("tipo_operacion")));
                transaccion.setFecha(rs.getTimestamp("momento_operacion"));
                transaccion.setMonto(rs.getDouble("monto_transaccion"));
                transaccion.setNumeroOperacionBancaria(rs.getString("numero_operacion_bancaria"));
                transaccion.setMedioPago(MedioPago.valueOf(rs.getString("medio_pago")));
                transaccion.setTipoCambio(rs.getDouble("valor_tipo_cambio"));
                if(transaccion.getCuentaOrigen() == null)
                    transaccion.setCuentaOrigen(new CuentaBancaria());
                transaccion.getCuentaOrigen().setIdCuenta(rs.getInt("id_cuenta_origen"));
                if(transaccion.getCuentaDestino() == null)
                    transaccion.setCuentaDestino(new CuentaBancaria());
                transaccion.getCuentaDestino().setIdCuenta(rs.getInt("id_cuenta_destino"));
                if(transaccion.getMoneda() == null)
                    transaccion.setMoneda(new Moneda());
                transaccion.getMoneda().setIdMoneda(rs.getInt("id_moneda"));
                transacciones.add(transaccion);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar caja chica por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }

        return transacciones;
    }
}