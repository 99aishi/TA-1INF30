package pe.edu.pucp.economix.tesoreria.implement;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.tesoreria.dao.IEntregaARendirDAO;
import pe.edu.pucp.economix.tesoreria.model.EntregaARendir;

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
            cs=con.prepareCall("{call pa_insertar_tes_entrega_rendir(?,?,?,?,?,?,?,?)}");
            cs.registerOutParameter("_id_fondo",Types.INTEGER);
            cs.setString("p_nombre_fondo", entrega.getNombre());
            cs.setDouble("p_monto_saldo_actual",entrega.getSaldoActual());
            cs.setString("p_estado_fondo",entrega.getEstado().name());

            cs.setString("p_motivo_entrega", entrega.getMotivo());
            cs.setDouble("p_monto_solicitado",entrega.getMontoSolicitado());
            Date fechaSql  = new Date(entrega.getFechaSolicitud().getTime());
            cs.setDate("p_fecha_solicitud",fechaSql);
            fechaSql= new Date(entrega.getFechaAperturaEntrega().getTime());
            cs.setDate("p_fecha_apertura",fechaSql);

            fechaSql= new Date(entrega.getFechaAperturaEntrega().getTime());
            cs.setDate("p_fecha_cierre",fechaSql);

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
    public int modificar(EntregaARendir objeto) {
        return 0;
    }

    @Override
    public int eliminar(int id) {
        return 0;
    }

    @Override
    public EntregaARendir buscarPorId(int id) {
        return null;
    }

    @Override
    public List<EntregaARendir> listarTodas() {
        return List.of();
    }
}
