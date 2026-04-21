package pe.edu.pucp.economix.tesoreria.implement;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.rrhh.model.Administrador;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.tesoreria.dao.ICuentaBancariaDAO;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;
import pe.edu.pucp.economix.tesoreria.model.Moneda;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaBancariaImplement implements ICuentaBancariaDAO{
    private Connection con;
    private Statement st;
    private PreparedStatement pst;
    private ResultSet rs;
    private CallableStatement cs;

    @Override
    public int insertar(CuentaBancaria cuentaBancaria) {
        int resultado=0;
        try{
            con = DBManager.getDBManager().getConnection();
            cs=con.prepareCall("{call pa_insertar_cuenta_bancaria(?,?,?,?,?,?)}");
            cs.registerOutParameter("_id_cuenta_bancaria",Types.INTEGER);
            cs.setString("_nombre_banco", cuentaBancaria.getNombreBanco());
            cs.setString("_numero_cuenta", cuentaBancaria.getNumeroBancario());
            cs.setString("_cci",cuentaBancaria.getCci());
            cs.setInt("_id_moneda",cuentaBancaria.getMoneda().getIdMoneda());
            cs.setInt("_id_usuario_titular",cuentaBancaria.getAdministrador().getUsuarioID());
            cs.executeUpdate();
            resultado=cs.getInt("_id_cuenta_bancaria");
        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }finally {
            try{
                cs.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
            try {
                con.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public int modificar(CuentaBancaria cuentaBancaria) {
        int resultado=0;
        try{
            con = DBManager.getDBManager().getConnection();
            cs=con.prepareCall("{call pa_modificar_cuenta_bancaria(?,?,?,?,?,?)}");
            cs.setInt("_id_cuenta_bancaria",cuentaBancaria.getIdCuenta());
            cs.setString("_nombre_banco", cuentaBancaria.getNombreBanco());
            cs.setString("_numero_cuenta", cuentaBancaria.getNumeroBancario());
            cs.setString("_cci",cuentaBancaria.getCci());
            cs.setInt("_id_moneda",cuentaBancaria.getMoneda().getIdMoneda());
            cs.setInt("_id_usuario_titular",cuentaBancaria.getAdministrador().getUsuarioID());
            resultado=cs.executeUpdate();;
        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }finally {
            try{
                cs.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
            try {
                con.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public int eliminar(int idCuentaBancaria) {
        int resultado=0;
        try {
            con = DBManager.getDBManager().getConnection();
            cs= con.prepareCall("{call pa_eliminar_cuenta_bancaria(?)}");
            cs.setInt("_id_cuenta_bancaria",idCuentaBancaria);
            resultado=cs.executeUpdate();
        }catch (Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }finally {
            try {
                cs.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
            try {
                con.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public CuentaBancaria buscarPorId(int idCuentaBancaria) {
        CuentaBancaria cuentaBancaria=null;
        try{
            con = DBManager.getDBManager().getConnection();
            cs= con.prepareCall("{call pa_busqueda_por_id_cuenta_bancaria(?)}");
            cs.setInt("_id_cuenta_bancaria",idCuentaBancaria);
            rs=cs.executeQuery();

            if(rs.next()){
                cuentaBancaria=new CuentaBancaria();
                cuentaBancaria.setIdCuenta(rs.getInt("id"));
                cuentaBancaria.setNumeroBancario(rs.getString("numero_cuenta"));
                cuentaBancaria.setNombreBanco(rs.getString("nombre_banco"));
                cuentaBancaria.setCci(rs.getString("cci"));
                Empleado emp=new Empleado();
                emp.setNombre(rs.getString("nombres"));
                emp.setApellidoPaterno(rs.getString("apellido_paterno"));
                emp.setApellidoMaterno(rs.getString("apellido_materno"));
                cuentaBancaria.setAdministrador(emp);
                Moneda mon=new Moneda();
                mon.setCodigoISO(rs.getString("codigo_iso"));
                cuentaBancaria.setMoneda(mon);
            }
        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }finally {
            try{
                cs.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
            try {
                con.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
        }
        return cuentaBancaria;
    }

    @Override
    public List<CuentaBancaria> listarTodas() {
        List<CuentaBancaria>cuentas=null;
        try{
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_listar_cuentas_bancarias()}");
            rs=cs.executeQuery();
            while(rs.next()){
                if(cuentas==null) cuentas=new ArrayList<>();
                CuentaBancaria cuentaBancaria =new CuentaBancaria();
                cuentaBancaria.setIdCuenta(rs.getInt("id"));
                cuentaBancaria.setNumeroBancario(rs.getString("numero_cuenta"));
                cuentaBancaria.setNombreBanco(rs.getString("nombre_banco"));
                cuentaBancaria.setCci(rs.getString("cci"));
                Empleado emp=new Empleado();
                emp.setNombre(rs.getString("nombres"));
                emp.setApellidoPaterno(rs.getString("apellido_paterno"));
                emp.setApellidoMaterno(rs.getString("apellido_materno"));
                cuentaBancaria.setAdministrador(emp);
                Moneda mon=new Moneda();
                mon.setCodigoISO(rs.getString("codigo_iso"));
                cuentaBancaria.setMoneda(mon);
                cuentas.add(cuentaBancaria);
            }
        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }finally {
            try{
                cs.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
            try {
                con.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
        }
        return cuentas;
    }
}
