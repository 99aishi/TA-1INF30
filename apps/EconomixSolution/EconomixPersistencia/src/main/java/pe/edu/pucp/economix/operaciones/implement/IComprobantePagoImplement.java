package pe.edu.pucp.economix.operaciones.implement;

import pe.edu.pucp.economix.operaciones.dao.IComprobantePagoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IComprobantePagoImplement {
    private Connection con;
    private Statement st;
    private PreparedStatement pst;
    private ResultSet rs;
    private CallableStatement cs;
}
