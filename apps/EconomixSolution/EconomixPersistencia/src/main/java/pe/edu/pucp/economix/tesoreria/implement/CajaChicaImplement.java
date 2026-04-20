package pe.edu.pucp.economix.tesoreria.implement;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.tesoreria.dao.ICajaChicaDAO;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;

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
    public int insertar(CajaChica objeto) {
        int resultado =0;

        try{
            con = DBManager.getDBManager().getConnection();

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
