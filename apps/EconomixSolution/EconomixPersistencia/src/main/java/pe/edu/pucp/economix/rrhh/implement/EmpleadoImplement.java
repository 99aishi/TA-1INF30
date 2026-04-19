package pe.edu.pucp.economix.rrhh.implement;

import pe.edu.pucp.economix.rrhh.dao.IEmpleadoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoImplement implements IEmpleadoDAO {
    private Connection con;
    private Statement st;
    private PreparedStatement pst;
    private ResultSet rs;
    private CallableStatement cs;
}
