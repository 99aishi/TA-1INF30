package pe.edu.pucp.economix.tesoreria.implement;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.tesoreria.dao.IMonedaDAO;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonedaImplement implements IMonedaDAO {
    private Connection con;
    private Statement st;
    private PreparedStatement pst;
    private ResultSet rs;
    private CallableStatement cs;

    @Override
    public int insertar(Moneda moneda) {
        int resultado=0;
        try{
            con = DBManager.getDBManager().getConnection();
            cs=con.prepareCall("{call pa_insertar_moneda(?,?,?)}");
            cs.registerOutParameter("_id_moneda",Types.INTEGER);
            cs.setString("_codigo_iso", moneda.getCodigoISO());
            cs.setString("_simbolo", moneda.getSimbolo());
            cs.executeUpdate();
            resultado=cs.getInt("_id_moneda");
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
    public int modificar(Moneda moneda) {
        int resultado=0;
        try{
            con = DBManager.getDBManager().getConnection();
            cs= con.prepareCall("{call pa_modificar_moneda(?,?,?)}");
            cs.setInt("_id_moneda",moneda.getIdMoneda());
            cs.setString("_codigo_iso", moneda.getCodigoISO());
            cs.setString("_simbolo", moneda.getSimbolo());
            resultado=cs.executeUpdate();
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
    public int eliminar(int idMoneda) {
        int resultado=0;
        try {
            con = DBManager.getDBManager().getConnection();
            cs= con.prepareCall("{call pa_eliminar_moneda(?)}");
            cs.setInt("_id_moneda",idMoneda);
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
    public Moneda buscarPorId(int idMoneda) {
        Moneda moneda=null;
        try{
            con = DBManager.getDBManager().getConnection();
            cs= con.prepareCall("{call pa_busqueda_por_id_moneda(?)}");
            cs.setInt("_id_moneda",idMoneda);
            rs=cs.executeQuery();
            
            if(rs.next()){
                moneda=new Moneda();
                moneda.setIdMoneda(rs.getInt("id_moneda"));
                moneda.setCodigoISO(rs.getString("codigo_iso"));
                moneda.setSimbolo(rs.getString("simbolo"));

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
        return moneda;
    }

    @Override
    public List<Moneda> listarTodas() {
        List<Moneda>monedas=null;
        try{
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_listar_monedas()}");
            rs=cs.executeQuery();
            while(rs.next()){
                if(monedas==null) monedas=new ArrayList<>();
                Moneda moneda =new Moneda();
                moneda.setIdMoneda(rs.getInt("id_moneda"));
                moneda.setCodigoISO(rs.getString("codigo_iso"));
                moneda.setSimbolo(rs.getString("simbolo"));
                monedas.add(moneda);
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
        return monedas;
    }
}
