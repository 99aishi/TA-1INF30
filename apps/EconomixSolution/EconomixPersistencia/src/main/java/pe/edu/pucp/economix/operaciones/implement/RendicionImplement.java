package pe.edu.pucp.economix.operaciones.implement;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.operaciones.dao.IRendicionDAO;
import pe.edu.pucp.economix.operaciones.model.EstadoRendicion;
import pe.edu.pucp.economix.operaciones.model.Rendicion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RendicionImplement implements IRendicionDAO{
    private Connection con;
    private ResultSet rs;
    private CallableStatement cs;

    @Override
    public int insertar(Rendicion rendicion){
        int id = 0;

        try{
            con = DBManager.getDBManager().getConnection();
            //Insertamos al usuario
            cs= con.prepareCall("{call pa_insertar_rendicion(?,?,?,?,?,?,?,?)}");

            cs.setDate("p_fecha_presentacion", new java.sql.Date( rendicion.getFechaPresentacion().getTime()));
            if (rendicion.getFechaAprobacion() != null)
                cs.setDate("p_fecha_aprobacion", new java.sql.Date( rendicion.getFechaAprobacion().getTime()));
            else
                cs.setNull("p_fecha_aprobacion", Types.DATE);
            cs.setDouble("p_monto_total_declarado", rendicion.getTotalDeclarado());
            cs.setDouble("p_monto_total_aprobado", rendicion.getTotalAprobado());
            cs.setDouble("p_monto_saldo_final", rendicion.getSaldoFinal());
            cs.setString("p_estado_rendicion", rendicion.getEstado().toString());
            cs.setString("p_comentario", rendicion.getComentario());
            cs.registerOutParameter("p_id_generado", java.sql.Types.INTEGER); // TODO Revisión INTEGER

            cs.executeUpdate();

            id = cs.getInt("p_id_generado");

            return id;
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

        return id;
    }
    @Override
    public int modificar(Rendicion rendicion){
        int cantidad = 0;

        try{
            con = DBManager.getDBManager().getConnection();
            //Insertamos al usuario
            cs= con.prepareCall("{call pa_modificar_rendicion(?,?,?,?,?,?,?,?)}");
            cs.setInt("p_id_rendicion", rendicion.getIdRendicion());
            cs.setDate("p_fecha_presentacion", new java.sql.Date( rendicion.getFechaPresentacion().getTime()));
            cs.setDate("p_fecha_aprobacion", new java.sql.Date( rendicion.getFechaAprobacion().getTime()));
            cs.setDouble("p_monto_total_declarado", rendicion.getTotalDeclarado());
            cs.setDouble("p_monto_total_aprobado", rendicion.getTotalAprobado());
            cs.setDouble("p_monto_saldo_final", rendicion.getSaldoFinal());
            cs.setString("p_estado_rendicion", rendicion.getEstado().toString());
            cs.setString("p_comentario", rendicion.getComentario());

            cantidad = cs.executeUpdate();

            return cantidad;
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

        return cantidad;
    }
    @Override
    public int eliminar(int idRendicion){
        int cantidad=0;

        try{
            con = DBManager.getDBManager().getConnection();
            //Insertamos al usuario
            cs= con.prepareCall("{call pa_eliminar_rendicion(?,?,?,?,?,?,?,?)}");
            cs.setInt("p_id_rendicion", idRendicion);

            cantidad = cs.executeUpdate();

            return cantidad;
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

        return cantidad;
    }
    @Override
    public Rendicion buscarPorId(int idRendicion){
        Rendicion rendicion = null;

        try{
            con = DBManager.getDBManager().getConnection();
            //Insertamos al usuario
            cs= con.prepareCall("{call pa_buscar_rendicion_por_id(?)}");
            cs.setInt("p_id_rendicion", idRendicion);

            rs = cs.executeQuery();
            if(rs.next()){
                rendicion = new Rendicion();
                rendicion.setIdRendicion(rs.getInt("id_rendicion"));
                rendicion.setFechaPresentacion(rs.getDate("fecha_presentacion"));
                rendicion.setFechaAprobacion(rs.getDate("fecha_aprobacion"));
                rendicion.setTotalDeclarado(rs.getDouble("monto_total_declarado"));
                rendicion.setTotalAprobado(rs.getDouble("monto_total_aprobado"));
                rendicion.setSaldoFinal(rs.getDouble("monto_saldo_final"));
                rendicion.setEstado(EstadoRendicion.valueOf(rs.getString("estado_rendicion")));
                rendicion.setComentario(rs.getString("comentario"));
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

        return rendicion;
    }
    @Override
    public List<Rendicion> listarTodas(){
        List rendiciones = null;

        try{
            con = DBManager.getDBManager().getConnection();
            //Insertamos al usuario
            cs= con.prepareCall("{call pa_listar_rendiciones()}");
            rs = cs.executeQuery();
            while(rs.next()){
                if(rendiciones == null)
                    rendiciones = new ArrayList<>();
                Rendicion rendicion = new Rendicion();
                rendicion.setIdRendicion(rs.getInt("id_rendicion"));
                rendicion.setFechaPresentacion(rs.getDate("fecha_presentacion"));
                rendicion.setFechaAprobacion(rs.getDate("fecha_aprobacion"));
                rendicion.setTotalDeclarado(rs.getDouble("monto_total_declarado"));
                rendicion.setTotalAprobado(rs.getDouble("monto_total_aprobado"));
                rendicion.setSaldoFinal(rs.getDouble("monto_saldo_final"));
                rendicion.setEstado(EstadoRendicion.valueOf(rs.getString("estado_rendicion")));
                rendicion.setComentario(rs.getString("comentario"));

                rendiciones.add(rendicion);
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

        return rendiciones;
    }

}
