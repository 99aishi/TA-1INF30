package pe.edu.pucp.economix.operaciones.implement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.operaciones.dao.IRendicionDAO;
import pe.edu.pucp.economix.operaciones.model.EstadoRendicion;
import pe.edu.pucp.economix.operaciones.model.Rendicion;

public class RendicionImplement implements IRendicionDAO{
    private ResultSet rs;

    @Override
    public int insertar(Rendicion rendicion) throws SQLException{
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_generado", Types.INTEGER);
        parametrosEntrada.put("p_fecha_presentacion", rendicion.getFechaPresentacion().getTime());
        parametrosEntrada.put("p_fecha_aprobacion", rendicion.getFechaAprobacion().getTime());
        parametrosEntrada.put("p_monto_total_declarado", rendicion.getTotalDeclarado());
        parametrosEntrada.put("p_monto_total_aprobado", rendicion.getTotalAprobado());
        parametrosEntrada.put("p_monto_saldo_final", rendicion.getSaldoFinal());
        parametrosEntrada.put("p_estado_rendicion", rendicion.getEstado().toString());
        parametrosEntrada.put("p_comentario", rendicion.getComentario());

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_rendicion", parametrosEntrada, parametrosSalida);
        rendicion.setIdRendicion((int)parametrosSalida.get("p_id_generado"));

        return rendicion.getIdRendicion();
    }
    @Override
    public int modificar(Rendicion rendicion) throws SQLException{
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_rendicion", rendicion.getIdRendicion());
        parametrosEntrada.put("p_fecha_presentacion", rendicion.getFechaPresentacion().getTime());
        parametrosEntrada.put("p_fecha_aprobacion", rendicion.getFechaAprobacion().getTime());
        parametrosEntrada.put("p_monto_total_declarado", rendicion.getTotalDeclarado());
        parametrosEntrada.put("p_monto_total_aprobado", rendicion.getTotalAprobado());
        parametrosEntrada.put("p_monto_saldo_final", rendicion.getSaldoFinal());
        parametrosEntrada.put("p_estado_rendicion", rendicion.getEstado().toString());
        parametrosEntrada.put("p_comentario", rendicion.getComentario());

        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_rendicion", parametrosEntrada, null);
        return resultado;
    }
    @Override
    public int eliminar(int idRendicion) throws SQLException{
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_rendicion", idRendicion);
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_eliminar_rendicion", parametrosEntrada, null);
        return resultado;
    }
    @Override
    public Rendicion buscarPorId(int idRendicion) throws SQLException{
        Rendicion rendicion = null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_rendicion", idRendicion);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_buscar_rendicion_por_id", parametrosEntrada);
        try{
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
        }catch(SQLException ex){
            System.out.println("Error al buscar caja chica por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }

        return rendicion;
    }
    @Override
    public List<Rendicion> listarTodas() throws SQLException{
        List<Rendicion> rendiciones = null;
        Rendicion rendicion;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_buscar_rendicion_por_id", null);
        try{
            while(rs.next()){
                if(rendiciones == null) rendiciones = new ArrayList<>();
                rendicion = new Rendicion();
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
        }catch(SQLException ex){
            System.out.println("Error al buscar caja chica por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }        return rendiciones;
    }

}
