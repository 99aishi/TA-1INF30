package pe.edu.pucp.economix.operaciones.implement;

import pe.edu.pucp.economix.operaciones.dao.ISolicitudGasto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SolicitudGastoImplement implements ISolicitudGasto {
    private Connection con;
    private Statement st;
    private PreparedStatement pst;
    private ResultSet rs;
    private CallableStatement cs;
}
