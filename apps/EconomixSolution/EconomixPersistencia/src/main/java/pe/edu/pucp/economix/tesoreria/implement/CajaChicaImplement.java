package pe.edu.pucp.economix.tesoreria.implement;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.tesoreria.dao.ICajaChicaDAO;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;
import pe.edu.pucp.economix.tesoreria.model.EstadoFondo;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CajaChicaImplement implements ICajaChicaDAO{
    private Connection con;
    private Statement st;
    private PreparedStatement pst;
    private ResultSet rs;
    private CallableStatement cs;

    @Override
    public int insertar(CajaChica cajaChica) {
        int resultado =0;

        try{
            con = DBManager.getDBManager().getConnection();
            cs=con.prepareCall("{call pa_insertar_tes_caja_chica(?,?,?,?,?,?)}");
            cs.registerOutParameter("_id_fondo",Types.INTEGER);
            cs.setString("p_nombre_fondo", cajaChica.getNombre());
            cs.setDouble("p_monto_saldo_actual",cajaChica.getSaldoActual());
            cs.setString("p_estado_fondo",cajaChica.getEstado().name());
            cs.setDouble("p_monto_techo",cajaChica.getMontoTecho());
            cs.setInt("p_id_area",cajaChica.getAreaAsignada().getIdArea());
            cs.executeUpdate();
            resultado=cs.getInt("_id_fondo");
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
    public int modificar(CajaChica objeto) {
        int resultado=0;
        try{
            con = DBManager.getDBManager().getConnection();
            cs=con.prepareCall("CALL pa_modificar_tes_caja_chica(?,?,?)");
            cs.setInt("p_id_fondo",objeto.getIdFondo());
            cs.setDouble("p_monto_saldo_actual",objeto.getSaldoActual());
            cs.setDouble("p_monto_techo",objeto.getMontoTecho());
            resultado = cs.executeUpdate();

        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }finally{
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
    public int eliminar(int id) {
        int resultado=0;
        try{
            con = DBManager.getDBManager().getConnection();
            cs=con.prepareCall("call pa_eliminar_caja_chica(?)");
            cs.setInt("p_id_fondo",id);
            resultado=cs.executeUpdate();



        }catch (Exception ex){
            System.out.println("ERROR: " + ex.getMessage());
        }finally {
            try{
                cs.close();
            }catch(Exception ex){
                System.out.println("ERROR: " + ex.getMessage());
            }
            try{
                con.close();
            }catch (Exception ex){
                System.out.println("ERROR: " + ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public CajaChica buscarPorId(int id) {
        CajaChica caja=null;
        try{
            con= DBManager.getDBManager().getConnection();
            cs=con.prepareCall("call pa_buscar_por_id_caja_chica(?)");
            cs.setInt("p_id_fondo",id);
            rs=cs.executeQuery();

            if(rs.next()){
                int idFondo = rs.getInt("id_fondo");
                String nombre= rs.getString("nombre_fondo");
                double saldoActual=rs.getDouble("monto_saldo_actual");
                EstadoFondo estado = EstadoFondo.valueOf(rs.getString("estado_fondo"));
                double monto_techo= rs.getDouble("monto_techo");
                int idArea=rs.getInt("id_area");
                Area area= new Area();
                area.setIdArea(idArea);
                caja = new CajaChica(idFondo ,nombre,saldoActual,estado,monto_techo,area);
            }

        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }finally{
            try{
                cs.close();
            }catch(Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
            try{
                con.close();
            }catch(Exception ex){
                System.out.println("ERROR: "+ex.getMessage());
            }
        }

        return caja;
    }

    @Override
    public List<CajaChica> listarTodas() {
        List<CajaChica> cajas = null;
        try{
            con= DBManager.getDBManager().getConnection();
            cs=con.prepareCall("call pa_listar_caja_chica()");
            rs=cs.executeQuery();

            while(rs.next()){
                if(cajas==null) cajas = new ArrayList<>();
                int idFondo = rs.getInt("id_fondo");
                String nombre= rs.getString("nombre_fondo");
                double saldoActual=rs.getDouble("monto_saldo_actual");
                EstadoFondo estado = EstadoFondo.valueOf(rs.getString("estado_fondo"));
                double monto_techo= rs.getDouble("monto_techo");
                int idArea=rs.getInt("id_area");
                Area area= new Area();
                area.setIdArea(idArea);
                CajaChica caja = new CajaChica(idFondo ,nombre,saldoActual,estado,monto_techo,area);
                cajas.add(caja);
            }

        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }finally{
            try{
                cs.close();
            }catch(Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
            try{
                con.close();
            }catch(Exception ex){
                System.out.println("ERROR: "+ex.getMessage());
            }
        }
        return cajas;
    }
}
