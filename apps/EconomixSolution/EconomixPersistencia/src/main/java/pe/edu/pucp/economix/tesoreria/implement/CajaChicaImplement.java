package pe.edu.pucp.economix.tesoreria.implement;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.tesoreria.dao.ICajaChicaDAO;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;
import pe.edu.pucp.economix.tesoreria.model.Fondo;

import java.sql.*;
import java.util.ArrayList;
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
            cs=con.prepareCall("{call pa_insertar_tes_caja_chica(?,?,?,?,?,?,?,?,?)}");
            cs.registerOutParameter("_id_fondo",Types.INTEGER);
            cs.setString("p_nombre_fondo", cajaChica.getNombreFondo());
            cs.setInt("p_id_cuenta_bancaria", cajaChica.getCuentaBancaria().getIdCuenta());
            cs.setDouble("p_monto_saldo_actual",cajaChica.getSaldoActual());
            //cs.setString("p_estado_fondo",cajaChica.getEstado());
            //cs.setInt("_id_moneda",cuentaBancaria.getMoneda().getIdMoneda());
            //cs.setInt("_id_usuario_titular",cuentaBancaria.getAdministrador().getUsuarioID());
            //cs.executeUpdate();
            resultado=cs.getInt("_id_cuenta_bancaria");
        }catch (Exception ex){

        }



        return resultado;
    }

    @Override
    public int modificar(CajaChica objeto) {
        return 0;
    }

    @Override
    public int eliminar(int id) {
        return 0;
    }

    @Override
    public CajaChica buscarPorId(int id) {
        return null;
    }

    @Override
    public List<CajaChica> listarTodas() {
        return List.of();
    }
}
