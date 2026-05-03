package pe.edu.pucp.economix.operaciones.implement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.operaciones.dao.ICicloCajaChicaDAO;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.EstadoCicloCaja;
import pe.edu.pucp.economix.operaciones.model.Rendicion;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;

public class CicloCajaChicaImplement implements ICicloCajaChicaDAO {
    private ResultSet rs;

    @Override
    public int insertar(CicloCajaChica cicloCajaChica) throws SQLException {
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_generado", Types.INTEGER);
        parametrosEntrada.put("p_numero_semana", cicloCajaChica.getNumeroSemana());
        parametrosEntrada.put("p_fecha_apertura", new java.sql.Date(cicloCajaChica.getFechaApertura().getTime()));
        if(cicloCajaChica.getFechaCierre()!= null)
            parametrosEntrada.put("p_fecha_cierre", new java.sql.Date(cicloCajaChica.getFechaCierre().getTime()));
        else
            parametrosEntrada.put("p_fecha_cierre", null);
        parametrosEntrada.put("p_monto_saldo_inicial", cicloCajaChica.getSaldoInicial());
        parametrosEntrada.put("p_monto_total_gastado", cicloCajaChica.getTotalGastado());
        parametrosEntrada.put("p_estado_ciclo", cicloCajaChica.getEstado().toString());
        parametrosEntrada.put("p_id_fondo_caja_chica", cicloCajaChica.getCajaChica().getIdFondo());
        if(cicloCajaChica.getRendicion()!= null)
            parametrosEntrada.put("p_id_rendicion", cicloCajaChica.getRendicion().getIdRendicion());
        else
            parametrosEntrada.put("p_id_rendicion", null);

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_ciclo_caja", parametrosEntrada, parametrosSalida);
        cicloCajaChica.setIdCicloCaja((int)parametrosSalida.get("p_id_generado"));

        return cicloCajaChica.getIdCicloCaja();
    }

    @Override
    public int modificar(CicloCajaChica cicloCajaChica) throws SQLException{
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_numero_semana", cicloCajaChica.getNumeroSemana());
        parametrosEntrada.put("p_fecha_apertura", new java.sql.Date(cicloCajaChica.getFechaApertura().getTime()));
        if(cicloCajaChica.getFechaCierre()!= null)
            parametrosEntrada.put("p_fecha_cierre", new java.sql.Date(cicloCajaChica.getFechaCierre().getTime()));
        else
            parametrosEntrada.put("p_fecha_cierre", null);
        parametrosEntrada.put("p_monto_saldo_inicial", cicloCajaChica.getSaldoInicial());
        parametrosEntrada.put("p_monto_total_gastado", cicloCajaChica.getTotalGastado());
        parametrosEntrada.put("p_estado_ciclo", cicloCajaChica.getEstado().toString());
        parametrosEntrada.put("p_id_fondo_caja_chica", cicloCajaChica.getCajaChica().getIdFondo());
        if(cicloCajaChica.getRendicion()!= null)
            parametrosEntrada.put("p_id_rendicion", cicloCajaChica.getRendicion().getIdRendicion());
        else
            parametrosEntrada.put("p_id_rendicion", null);
        
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_ciclo_caja", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public int eliminar(int idCicloCaja) throws SQLException{
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_ciclo_caja", idCicloCaja);
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_eliminar_ciclo_caja", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public CicloCajaChica buscarPorId(int idCicloCaja) throws SQLException{
        CicloCajaChica ciclo = null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_ciclo_caja", idCicloCaja);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_buscar_ciclo_caja_por_id", parametrosEntrada);
        try{
            if(rs.next()){
                ciclo = new CicloCajaChica();
                ciclo.setIdCicloCaja(rs.getInt("id_ciclo_caja"));
                ciclo.setNumeroSemana(rs.getInt("numero_semana"));
                ciclo.setFechaApertura(rs.getDate("fecha_apertura"));
                ciclo.setFechaCierre(rs.getDate("fecha_cierre"));
                ciclo.setSaldoInicial(rs.getDouble("monto_saldo_inicial"));
                ciclo.setTotalGastado(rs.getDouble("monto_total_gastado"));
                ciclo.setEstado(EstadoCicloCaja.valueOf(rs.getString("estado_ciclo"))); 
                if(ciclo.getCajaChica() == null)
                    ciclo.setCajaChica(new CajaChica());
                ciclo.getCajaChica().setIdFondo(rs.getInt("id_fondo_caja_chica"));
                if(ciclo.getRendicion() == null)
                    ciclo.setRendicion(new Rendicion());
                ciclo.getRendicion().setIdRendicion(rs.getInt("id_rendicion"));
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar caja chica por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return ciclo;
    }

    @Override
    public List<CicloCajaChica> listarTodas() throws SQLException{
        List<CicloCajaChica> ciclos = null;
        CicloCajaChica ciclo;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_ciclos_caja", null);
        try{
            while(rs.next()){
                if(ciclos == null) ciclos = new ArrayList<>();
                ciclo = new CicloCajaChica();
                ciclo.setIdCicloCaja(rs.getInt("id_ciclo_caja"));
                ciclo.setNumeroSemana(rs.getInt("numero_semana"));
                ciclo.setFechaApertura(rs.getDate("fecha_apertura"));
                ciclo.setFechaCierre(rs.getDate("fecha_cierre"));
                ciclo.setSaldoInicial(rs.getDouble("monto_saldo_inicial"));
                ciclo.setTotalGastado(rs.getDouble("monto_total_gastado"));
                ciclo.setEstado(EstadoCicloCaja.valueOf(rs.getString("estado_ciclo"))); 
                if(ciclo.getCajaChica() == null)
                    ciclo.setCajaChica(new CajaChica());
                ciclo.getCajaChica().setIdFondo(rs.getInt("id_fondo_caja_chica"));
                if(ciclo.getRendicion() == null)
                    ciclo.setRendicion(new Rendicion());
                ciclo.getRendicion().setIdRendicion(rs.getInt("id_rendicion"));
                ciclos.add(ciclo);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar caja chica por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }

        return ciclos;
    }
}

