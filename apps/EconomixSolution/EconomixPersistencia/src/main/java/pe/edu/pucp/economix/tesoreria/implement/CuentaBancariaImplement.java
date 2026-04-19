package pe.edu.pucp.economix.tesoreria.implement;

import pe.edu.pucp.economix.tesoreria.dao.ICuentaBancariaDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaBancariaImplement implements ICuentaBancariaDAO {
    private Connection con;
    private Statement st;
    private PreparedStatement pst;
    private ResultSet rs;
    private CallableStatement cs;
}
