package pe.edu.pucp.economix.tesoreria.implement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.tesoreria.dao.ICuentaBancariaDAO;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

public class CuentaBancariaImplement implements ICuentaBancariaDAO{
    private ResultSet rs;

    @Override
    public int insertar(CuentaBancaria cuentaBancaria) throws SQLException {
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_cuenta_bancaria", Types.INTEGER);
        parametrosEntrada.put("p_nombre_banco", cuentaBancaria.getNombreBanco());
        parametrosEntrada.put("p_numero_cuenta", cuentaBancaria.getNumeroBancario());
        parametrosEntrada.put("p_cci", cuentaBancaria.getCci());
        if(cuentaBancaria.getMoneda() == null)
            parametrosEntrada.put("p_id_moneda", null);
        else
            parametrosEntrada.put("p_id_moneda", cuentaBancaria.getMoneda().getIdMoneda());
        if(cuentaBancaria.getEmpleadoAdministrador() == null)
            parametrosEntrada.put("p_id_usuario_titular", null);
        else
            parametrosEntrada.put("p_id_usuario_titular", cuentaBancaria.getEmpleadoAdministrador().getUsuarioID());
        if(cuentaBancaria.getAreaAdministradora() == null)
            parametrosEntrada.put("p_id_area_titular", null);
        else
            parametrosEntrada.put("p_id_area_titular", cuentaBancaria.getAreaAdministradora().getIdArea());

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_cuenta_bancaria", parametrosEntrada, parametrosSalida);
        cuentaBancaria.setIdCuenta((int)parametrosSalida.get("p_id_cuenta_bancaria"));

        return cuentaBancaria.getIdCuenta();
    }

    @Override
    public int modificar(CuentaBancaria cuentaBancaria) throws SQLException {
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_nombre_banco", cuentaBancaria.getNombreBanco());
        parametrosEntrada.put("p_numero_cuenta", cuentaBancaria.getNumeroBancario());
        parametrosEntrada.put("p_cci", cuentaBancaria.getCci());
        if(cuentaBancaria.getMoneda() == null)
            parametrosEntrada.put("p_id_moneda", null);
        else
            parametrosEntrada.put("p_id_moneda", cuentaBancaria.getMoneda().getIdMoneda());
        if(cuentaBancaria.getEmpleadoAdministrador() == null)
            parametrosEntrada.put("p_id_usuario_titular", null);
        else
            parametrosEntrada.put("p_id_usuario_titular", cuentaBancaria.getEmpleadoAdministrador().getUsuarioID());
        if(cuentaBancaria.getAreaAdministradora() == null)
            parametrosEntrada.put("p_id_area_titular", null);
        else
            parametrosEntrada.put("p_id_area_titular", cuentaBancaria.getAreaAdministradora().getIdArea());
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_cuenta_bancaria", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public int eliminar(int idCuentaBancaria) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_cuenta_bancaria", idCuentaBancaria);
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_eliminar_cuenta_bancaria", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public CuentaBancaria buscarPorId(int idCuentaBancaria) throws SQLException {
        CuentaBancaria cuentaBancaria=null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_cuenta_bancaria", idCuentaBancaria);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_buscar_cuenta_bancaria_por_id", parametrosEntrada);
        try{
            if(rs.next()){
                cuentaBancaria = new CuentaBancaria();
                cuentaBancaria.setIdCuenta(rs.getInt("id_cuenta_bancaria"));
                cuentaBancaria.setNumeroBancario(rs.getString("numero_cuenta"));
                cuentaBancaria.setNombreBanco(rs.getString("nombre_banco"));
                cuentaBancaria.setCci(rs.getString("cci"));
                if(cuentaBancaria.getMoneda() == null)
                    cuentaBancaria.setMoneda(new Moneda());
                cuentaBancaria.getMoneda().setIdMoneda(rs.getInt("id_moneda"));
                if(cuentaBancaria.getAreaAdministradora() == null)
                    cuentaBancaria.setAreaAdministradora(new Area());
                cuentaBancaria.getAreaAdministradora().setIdArea(rs.getInt("id_area"));
                if(cuentaBancaria.getEmpleadoAdministrador() == null)
                    cuentaBancaria.setEmpleadoAdministrador(new Empleado());
                cuentaBancaria.getEmpleadoAdministrador().setUsuarioID(rs.getInt("id_usuario"));
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar cuenta bancaria por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return cuentaBancaria;
    }

    @Override
    public List<CuentaBancaria> listarTodas() throws SQLException {
        List<CuentaBancaria>cuentas=null;
        CuentaBancaria cuentaBancaria;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_cuentas_bancarias", null);
        try{
            while(rs.next()){
                if(cuentas == null) cuentas = new ArrayList<>();
                cuentaBancaria = new CuentaBancaria();
                cuentaBancaria.setIdCuenta(rs.getInt("id_cuenta_bancaria"));
                cuentaBancaria.setNumeroBancario(rs.getString("numero_cuenta"));
                cuentaBancaria.setNombreBanco(rs.getString("nombre_banco"));
                cuentaBancaria.setCci(rs.getString("cci"));
                if(cuentaBancaria.getMoneda() == null)
                    cuentaBancaria.setMoneda(new Moneda());
                cuentaBancaria.getMoneda().setIdMoneda(rs.getInt("id_moneda"));
                if(cuentaBancaria.getAreaAdministradora() == null)
                    cuentaBancaria.setAreaAdministradora(new Area());
                cuentaBancaria.getAreaAdministradora().setIdArea(rs.getInt("id_area"));
                if(cuentaBancaria.getEmpleadoAdministrador() == null)
                    cuentaBancaria.setEmpleadoAdministrador(new Empleado());
                cuentaBancaria.getEmpleadoAdministrador().setUsuarioID(rs.getInt("id_usuario"));
                cuentas.add(cuentaBancaria);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar cuenta bancaria por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return cuentas;
    }
}
