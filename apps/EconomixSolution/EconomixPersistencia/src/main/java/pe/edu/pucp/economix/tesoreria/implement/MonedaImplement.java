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
            String sql = "INSERT INTO CC_MONEDA(CODIGO_ISO_MONEDA,SIMBOLO_MONEDA,FECHA_CREACION,FECHA_MODIFICACION) " +
                    "VALUES (?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1,moneda.getCodigoISO());
            pst.setString(2,moneda.getSimbolo());
            pst.setDate(3, new java.sql.Date(moneda.getFechaCreacion().getTime()));
            pst.setDate(4, new java.sql.Date(moneda.getFechaModificacion().getTime()));
            resultado=pst.executeUpdate();
        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }finally {
            try{
                pst.close();
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
            String sql = "UPDATE CC_MONEDA SET CODIGO_ISO_MONEDA = ?,"+"\n"+"SIMBOLO_MONEDA = ?,"+"\n"+"FECHA_MODIFICACION = ?"+"\n"+
                    "WHERE ID_MONEDA = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1,moneda.getCodigoISO());
            pst.setString(2,moneda.getSimbolo());
            pst.setDate(3, new java.sql.Date(moneda.getFechaModificacion().getTime()));
            pst.setInt(4, moneda.getIdMoneda());
            resultado=pst.executeUpdate();
        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }finally {
            try{
                pst.close();
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
        return 0;
    }

    @Override
    public Moneda buscarPorId(int idMoneda) {
        Moneda moneda=null;
        try{
            con = DBManager.getDBManager().getConnection();

            String sql = "SELECT ID_MONEDA,CODIGO_ISO_MONEDA,SIMBOLO_MONEDA,FECHA_CREACION,FECHA_MODIFICACION " +
                    "FROM CC_MONEDA"+"\n"+"WHERE ID_MONEDA=?";
            pst = con.prepareStatement(sql);

            pst.setInt(1, idMoneda);
            rs=pst.executeQuery();
            if(rs.next()){
                moneda=new Moneda();
                moneda.setIdMoneda(rs.getInt("ID_MONEDA"));
                moneda.setCodigoISO(rs.getString("CODIGO_ISO_MONEDA"));
                moneda.setSimbolo(rs.getString("SIMBOLO_MONEDA"));
                moneda.setFechaCreacion(rs.getDate("FECHA_CREACION"));
                moneda.setFechaModificacion(rs.getDate("FECHA_MODIFICACION"));
            }
        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }finally {
            try{
                pst.close();
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
            String sql = "SELECT ID_MONEDA,CODIGO_ISO_MONEDA,SIMBOLO_MONEDA,FECHA_CREACION,FECHA_MODIFICACION " +
                    "FROM CC_MONEDA";
            pst = con.prepareStatement(sql);
            rs=pst.executeQuery();
            while(rs.next()){
                if(monedas==null)monedas=new ArrayList<>();
                Moneda moneda =new Moneda();
                moneda.setIdMoneda(rs.getInt("ID_MONEDA"));
                moneda.setCodigoISO(rs.getString("CODIGO_ISO_MONEDA"));
                moneda.setSimbolo(rs.getString("SIMBOLO_MONEDA"));
                moneda.setFechaCreacion(rs.getDate("FECHA_CREACION"));
                moneda.setFechaModificacion(rs.getDate("FECHA_MODIFICACION"));
                monedas.add(moneda);
            }
        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }finally {
            try{
                pst.close();
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
