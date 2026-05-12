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
import pe.edu.pucp.economix.tesoreria.dao.ICajaChicaDAO;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;
import pe.edu.pucp.economix.tesoreria.model.EstadoFondo;

public class CajaChicaImplement implements ICajaChicaDAO{
    private ResultSet rs;

    @Override
    public int insertar(CajaChica cajaChica) throws SQLException {
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_fondo", Types.INTEGER);
        parametrosEntrada.put("p_nombre_fondo", cajaChica.getNombre());
        parametrosEntrada.put("p_estado_fondo", cajaChica.getEstado().toString());

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_fondo", parametrosEntrada, parametrosSalida);
        cajaChica.setIdFondo((int)parametrosSalida.get("p_id_fondo"));
        parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_fondo", cajaChica.getIdFondo());
        parametrosEntrada.put("p_monto_techo", (Double)cajaChica.getMontoTecho());
        if(cajaChica.getAreaAsignada() == null)
            parametrosEntrada.put("p_id_area", null);
        else
            parametrosEntrada.put("p_id_area", cajaChica.getAreaAsignada().getIdArea());

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_caja_chica", parametrosEntrada,null);

        return cajaChica.getIdFondo();
    }

    @Override
    public int modificar(CajaChica cajaChica) throws SQLException{
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_fondo", Types.INTEGER);
        parametrosEntrada.put("p_nombre_fondo", cajaChica.getAreaAsignada());
        parametrosEntrada.put("p_estado_fondo", cajaChica.getEstado());
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_fondo", parametrosEntrada, null);
        parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_fondo", cajaChica.getIdFondo());
        parametrosEntrada.put("p_monto_techo", (Double)cajaChica.getMontoTecho());
        if(cajaChica.getAreaAsignada() == null)
            parametrosEntrada.put("p_id_area", null);
        else
            parametrosEntrada.put("p_id_area", cajaChica.getAreaAsignada().getIdArea());
        
        DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_caja_chica", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public int eliminar(int id) throws SQLException{
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_fondo", id);
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_eliminar_caja_chica", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public CajaChica buscarPorId(int idCajaChica) throws SQLException{
        CajaChica caja=null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_fondo", idCajaChica);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_buscar_caja_chica_por_id", parametrosEntrada);
        try{
            if(rs.next()){
                caja = new CajaChica();
                caja.setIdFondo(rs.getInt("id_fondo"));
                caja.setNombre(rs.getString("nombre_fondo"));
                caja.setMontoTecho(rs.getDouble("monto_techo"));
                caja.setEstado(EstadoFondo.Activo);
                if(caja.getAreaAsignada() == null)
                    caja.setAreaAsignada(new Area());
                caja.getAreaAsignada().setIdArea(rs.getInt("id_area"));
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar caja chica por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return caja;
    }

    @Override
    public List<CajaChica> listarTodas() throws SQLException{
        List<CajaChica> cajas = null;
        CajaChica caja;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_cajas_chicas", null);
        try{
            while(rs.next()){
                if(cajas == null) cajas = new ArrayList<>();
                caja = new CajaChica();
                caja.setIdFondo(rs.getInt("id_fondo"));
                caja.setNombre(rs.getString("nombre_fondo"));
                caja.setMontoTecho(rs.getDouble("monto_techo"));
                caja.setEstado(EstadoFondo.Activo);
                if(caja.getAreaAsignada() == null)
                    caja.setAreaAsignada(new Area());
                caja.getAreaAsignada().setIdArea(rs.getInt("id_area"));
                cajas.add(caja);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar cajas chicas: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return cajas;
    }
}
