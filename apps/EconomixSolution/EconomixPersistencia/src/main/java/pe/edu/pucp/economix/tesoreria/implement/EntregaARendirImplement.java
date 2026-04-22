package pe.edu.pucp.economix.tesoreria.implement;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.tesoreria.dao.IEntregaARendirDAO;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;
import pe.edu.pucp.economix.tesoreria.model.EntregaARendir;
import pe.edu.pucp.economix.tesoreria.model.EstadoEntregaARendir;
import pe.edu.pucp.economix.tesoreria.model.EstadoFondo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntregaARendirImplement implements IEntregaARendirDAO  {
    private Connection con;
    private Statement st;
    private PreparedStatement pst;
    private ResultSet rs;
    private CallableStatement cs;


    @Override
    public int insertar(EntregaARendir entrega) {
        int resultado =0;

        try{
            con = DBManager.getDBManager().getConnection();
            cs=con.prepareCall("{call pa_insertar_tes_entrega_rendir(?,?,?,?,?,?,?,?,?,?,?,?)}");
            cs.registerOutParameter("_id_fondo",Types.INTEGER);
            cs.setString("p_nombre_fondo", entrega.getNombre());
            cs.setDouble("p_monto_saldo_actual",entrega.getSaldoActual());
            cs.setString("p_estado_fondo",entrega.getEstado().name());
            cs.setString("p_motivo_entrega", entrega.getMotivo());
            cs.setDouble("p_monto_solicitado",entrega.getMontoSolicitado());
            Date fechaSql =null;
            if(entrega.getFechaSolicitud() != null)
                fechaSql  = new Date(entrega.getFechaSolicitud().getTime());
            cs.setDate("p_fecha_solicitud",fechaSql);

            if(entrega.getFechaAperturaEntrega() != null)
                fechaSql= new Date(entrega.getFechaAperturaEntrega().getTime());
            else
                fechaSql=null;
            cs.setDate("p_fecha_apertura",fechaSql);

            if(entrega.getFechaCierreEntrega() != null)
                fechaSql= new Date(entrega.getFechaCierreEntrega().getTime());
            else
                fechaSql=null;
            cs.setDate("p_fecha_cierre",fechaSql);

            cs.setString("p_estado_entrega",entrega.getEstadoEntrega().name());
            cs.setInt("p_id_usuario_solicitante", entrega.getSolicitante().getUsuarioID());
            cs.setInt("p_id_usuario_aprobador",entrega.getAprobador().getUsuarioID());


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
    public int modificar(EntregaARendir entrega) {
        int resultado=0;
        try{
            con = DBManager.getDBManager().getConnection();
            cs=con.prepareCall("CALL pa_modificar_entrega_rendir(?,?,?,?)"); //idfondo, fecha apertura, fechacierre,estadoentrega
            cs.setInt("p_id_fondo",entrega.getIdFondo());
            Date fechaSql;
            if(entrega.getFechaAperturaEntrega() != null)
                fechaSql= new Date(entrega.getFechaAperturaEntrega().getTime());
            else
                fechaSql=null;
            cs.setDate("p_fecha_apertura",fechaSql);

            if(entrega.getFechaCierreEntrega() != null)
                fechaSql= new Date(entrega.getFechaCierreEntrega().getTime());
            else
                fechaSql=null;
            cs.setDate("p_fecha_cierre",fechaSql);

            cs.setString("p_estado_entrega",entrega.getEstadoEntrega().name());

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
            cs=con.prepareCall("call pa_eliminar_entrega_rendir(?)");
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
    public EntregaARendir buscarPorId(int id) {
        EntregaARendir entrega=null;
        try{
            con= DBManager.getDBManager().getConnection();
            cs=con.prepareCall("call pa_buscar_entrega_por_id(?)");
            cs.setInt("p_id_fondo",id);
            rs=cs.executeQuery();

            if(rs.next()){
                int idFondo = rs.getInt("id_fondo");
                String nombre= rs.getString("nombre_fondo");
                double saldoActual=rs.getDouble("monto_saldo_actual");
                EstadoFondo estadoFondo = EstadoFondo.valueOf(rs.getString("estado_fondo"));
                String motivo= rs.getString("motivo_entrega");
                double montoSolicitado= rs.getDouble("monto_solicitado");
                java.util.Date fechaSolicitud = rs.getDate("fecha_solicitud");
                java.util.Date fechaAperturaEntrega = rs.getDate("fecha_apertura");
                java.util.Date fechaCierreEntrega = rs.getDate("fecha_cierre");
                EstadoEntregaARendir estadoEntrega = EstadoEntregaARendir.valueOf( rs.getString("estado_entrega"));

                int idsoli= rs.getInt("id_usuario_solicitante");
                Empleado solicitante = new Empleado();
                solicitante.setUsuarioID(idsoli);
                int idaprobador= rs.getInt("id_usuario_aprobador");
                Empleado aprobador = new Empleado();
                aprobador.setUsuarioID(idaprobador);

                entrega = new EntregaARendir(idFondo ,nombre,saldoActual,estadoFondo,motivo,montoSolicitado,
                        fechaSolicitud,fechaAperturaEntrega,fechaCierreEntrega,estadoEntrega,
                            solicitante,aprobador
                        );
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

        return entrega;
    }

    @Override
    public List<EntregaARendir> listarTodas() {
        List<EntregaARendir> entregas = null;
        try{
            con= DBManager.getDBManager().getConnection();
            cs=con.prepareCall("call pa_listar_entrega_rendir()");
            rs=cs.executeQuery();

            while(rs.next()){
                if(entregas==null)
                    entregas = new ArrayList<>();

                //EntregaARendir entrega = new EntregaARendir();

                int idFondo = rs.getInt("id_fondo");
                String nombre= rs.getString("nombre_fondo");
                double saldoActual=rs.getDouble("monto_saldo_actual");
                EstadoFondo estadoFondo = EstadoFondo.valueOf(rs.getString("estado_fondo"));
                String motivo= rs.getString("motivo_entrega");
                double montoSolicitado= rs.getDouble("monto_solicitado");
                java.util.Date fechaSolicitud = rs.getDate("fecha_solicitud");
                java.util.Date fechaAperturaEntrega = rs.getDate("fecha_apertura");
                java.util.Date fechaCierreEntrega = rs.getDate("fecha_cierre");
                EstadoEntregaARendir estadoEntrega = EstadoEntregaARendir.valueOf( rs.getString("estado_entrega"));

                int idsoli= rs.getInt("id_usuario_solicitante");
                Empleado solicitante = new Empleado();
                solicitante.setUsuarioID(idsoli);
                int idaprobador= rs.getInt("id_usuario_aprobador");
                Empleado aprobador = new Empleado();
                aprobador.setUsuarioID(idaprobador);

                EntregaARendir entrega = new EntregaARendir(idFondo ,nombre,saldoActual,estadoFondo,motivo,montoSolicitado,
                        fechaSolicitud,fechaAperturaEntrega,fechaCierreEntrega,estadoEntrega,
                        solicitante,aprobador
                );
                entregas.add(entrega);
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
        return entregas;
    }
}
