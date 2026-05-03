package pe.edu.pucp.economix.tesoreria.implement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.tesoreria.dao.IMonedaDAO;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

public class MonedaImplement implements IMonedaDAO {
    private ResultSet rs;

    @Override
    public int insertar(Moneda moneda) throws SQLException {
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_moneda", Types.INTEGER);
        parametrosEntrada.put("p_codigo_iso", moneda.getCodigoISO());
        parametrosEntrada.put("p_simbolo", moneda.getSimbolo());

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_moneda", parametrosEntrada, parametrosSalida);
        moneda.setIdMoneda((int)parametrosSalida.get("p_id_moneda"));

        return moneda.getIdMoneda();
    }

    @Override
    public int modificar(Moneda moneda) throws SQLException {
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_moneda", moneda.getIdMoneda());
        parametrosEntrada.put("p_codigo_iso", moneda.getCodigoISO());
        parametrosEntrada.put("p_simbolo", moneda.getSimbolo());
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_moneda", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public int eliminar(int idMoneda) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_moneda", idMoneda);
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_eliminar_moneda", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public Moneda buscarPorId(int idMoneda) throws SQLException {
        Moneda moneda=null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_moneda", idMoneda);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_buscar_moneda_por_id", parametrosEntrada);
        try{
            if(rs.next()){
                moneda = new Moneda();
                moneda.setIdMoneda(rs.getInt("id_moneda"));
                moneda.setCodigoISO(rs.getString("codigo_iso"));
                moneda.setSimbolo(rs.getString("simbolo"));
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar moneda por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return moneda;
    }

    @Override
    public List<Moneda> listarTodas() throws SQLException {
        List<Moneda>monedas=null;
        Moneda moneda;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_monedas", null);
        try{
            while(rs.next()){
                if(monedas == null) monedas = new ArrayList<>();
                moneda = new Moneda();
                moneda.setIdMoneda(rs.getInt("id_moneda"));
                moneda.setCodigoISO(rs.getString("codigo_iso"));
                moneda.setSimbolo(rs.getString("simbolo"));
                monedas.add(moneda);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar monedas: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return monedas;
    }
}
