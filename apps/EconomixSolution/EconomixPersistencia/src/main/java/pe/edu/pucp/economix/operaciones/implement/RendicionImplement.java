package pe.edu.pucp.economix.operaciones.implement;

import pe.edu.pucp.economix.operaciones.dao.IRendicionDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RendicionImplement implements IRendicionDAO {
    private Connection con;
    private Statement st;
    private PreparedStatement pst;
    private ResultSet rs;
    private CallableStatement cs;

}
