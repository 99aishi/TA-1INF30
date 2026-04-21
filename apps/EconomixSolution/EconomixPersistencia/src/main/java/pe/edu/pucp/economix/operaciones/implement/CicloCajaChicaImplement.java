package pe.edu.pucp.economix.operaciones.implement;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.operaciones.dao.ICicloCajaChicaDAO;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.EstadoCicloCaja;
import pe.edu.pucp.economix.tesoreria.dao.ICajaChicaDAO;
import pe.edu.pucp.economix.tesoreria.implement.CajaChicaImplement;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CicloCajaChicaImplement implements ICicloCajaChicaDAO {
    private Connection con;
    private Statement st;
    private PreparedStatement pst;
    private ResultSet rs;
    private CallableStatement cs;

    @Override
    public int insertar(CicloCajaChica cicloCajaChica) {
        int id=0;
        try{
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_insertar_ciclo_caja(?,?,?,?,?,?,?,?,?)}");

            cs.setInt("p_numero_semana", cicloCajaChica.getNumeroSemana());
            cs.setDate("p_fecha_apertura", new java.sql.Date(cicloCajaChica.getFechaApertura().getTime()));
            if(cicloCajaChica.getFechaCierre()!= null)
                cs.setDate("p_fecha_cierre", new java.sql.Date(cicloCajaChica.getFechaCierre().getTime()));
            else
                cs.setNull("p_fecha_cierre", Types.DATE);
            cs.setDouble("p_monto_saldo_inicial", cicloCajaChica.getSaldoInicial());
            cs.setDouble("p_monto_total_gastado", cicloCajaChica.getTotalGastado());
            cs.setString("p_estado_ciclo", cicloCajaChica.getEstado().toString());
            cs.setInt("p_id_fondo_caja_chica", cicloCajaChica.getCajaChica().getIdFondo());
            cs.setNull("p_id_rendicion", Types.INTEGER); // sin rendicion al crear
            cs.registerOutParameter("p_id_generado", java.sql.Types.INTEGER);

            cs.executeUpdate();

            id = cs.getInt("p_id_generado");
        } catch (Exception ex){
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try {
                cs.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
            try {
                con.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }
        return id;
    }

    @Override
    public int modificar(CicloCajaChica cicloCajaChica) {
        int cantidad = 0;

        try {
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_modificar_ciclo_caja(?,?,?,?,?,?,?,?,?)}");

            cs.setInt("p_id_ciclo_caja", cicloCajaChica.getIdCicloCaja());
            cs.setInt("p_numero_semana", cicloCajaChica.getNumeroSemana());
            cs.setDate("p_fecha_apertura", new java.sql.Date(cicloCajaChica.getFechaApertura().getTime()));
            if (cicloCajaChica.getFechaCierre() != null)
                cs.setDate("p_fecha_cierre", new java.sql.Date(cicloCajaChica.getFechaCierre().getTime()));
            else
                cs.setNull("p_fecha_cierre", Types.DATE);
            cs.setDouble("p_monto_saldo_inicial", cicloCajaChica.getSaldoInicial());
            cs.setDouble("p_monto_total_gastado", cicloCajaChica.getTotalGastado());
            cs.setString("p_estado_ciclo", cicloCajaChica.getEstado().toString());
            cs.setInt("p_id_fondo_caja_chica", cicloCajaChica.getCajaChica().getIdFondo());
            cs.setNull("p_id_rendicion", Types.INTEGER);

            cantidad = cs.executeUpdate();

            return cantidad;
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try {
                cs.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
            try {
                con.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }

        return cantidad;
    }

    @Override
    public int eliminar(int idCicloCaja) {
        int cantidad = 0;

        try {
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_eliminar_ciclo_caja(?)}");
            cs.setInt("p_id_ciclo_caja", idCicloCaja);

            cantidad = cs.executeUpdate();

            return cantidad;
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try {
                cs.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
            try {
                con.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }

        return cantidad;
    }

    @Override
    public CicloCajaChica buscarPorId(int idCicloCaja) {
        CicloCajaChica ciclo = null;

        try {
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_buscar_ciclo_caja_por_id(?)}");
            cs.setInt("p_id_ciclo_caja", idCicloCaja);

            rs = cs.executeQuery();
            if (rs.next()) {
                ciclo = new CicloCajaChica(
                        rs.getInt("numero_semana"),
                        rs.getDate("fecha_apertura"),
                        rs.getDate("fecha_cierre"),
                        rs.getDouble("monto_saldo_inicial"),
                        null // CajaChica se puede cargar aparte si se necesita
                );
                ciclo.setIdCicloCaja(rs.getInt("id_ciclo_caja"));
                ciclo.setTotalGastado(rs.getDouble("monto_total_gastado"));
                ciclo.setEstado(EstadoCicloCaja.valueOf(rs.getString("estado_ciclo")));

                // Para el FK de CajaChica, llamamos a su Implement
                int idCajaChica = rs.getInt("id_fondo_caja_chica");
                ciclo.setCajaChica(new CajaChicaImplement().buscarPorId(idCajaChica));
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try {
                cs.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
            try {
                con.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }

        return ciclo;
    }

    @Override
    public List<CicloCajaChica> listarTodas() {
        List<CicloCajaChica> ciclos = null;

        try {
            con = DBManager.getDBManager().getConnection();
            cs = con.prepareCall("{call pa_listar_ciclos_caja()}");

            rs = cs.executeQuery();
            while (rs.next()) {
                if (ciclos == null)
                    ciclos = new ArrayList<>();

                CicloCajaChica ciclo = new CicloCajaChica(
                        rs.getInt("numero_semana"),
                        rs.getDate("fecha_apertura"),
                        rs.getDate("fecha_cierre"),
                        rs.getDouble("monto_saldo_inicial"),
                        null
                );
                ciclo.setIdCicloCaja(rs.getInt("id_ciclo_caja"));
                ciclo.setTotalGastado(rs.getDouble("monto_total_gastado"));
                ciclo.setEstado(EstadoCicloCaja.valueOf(rs.getString("estado_ciclo")));

                int idCajaChica = rs.getInt("id_fondo_caja_chica");
                ciclo.setCajaChica(new CajaChicaImplement().buscarPorId(idCajaChica));

                ciclos.add(ciclo);
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try {
                cs.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
            try {
                con.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }

        return ciclos;
    }
}

