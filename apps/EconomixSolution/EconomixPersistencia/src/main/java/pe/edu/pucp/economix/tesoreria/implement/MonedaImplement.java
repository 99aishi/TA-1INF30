package pe.edu.pucp.economix.tesoreria.implement;

import pe.edu.pucp.economix.tesoreria.dao.IMonedaDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonedaImplement implements IMonedaDAO {
    private Connection con;
    private Statement st;
    private PreparedStatement pst;
    private ResultSet rs;
    private CallableStatement cs;
}
