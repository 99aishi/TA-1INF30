package pe.edu.pucp.economix.operaciones.implement;

import pe.edu.pucp.economix.operaciones.dao.ITransaccion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaccionImplement implements ITransaccion {
    private Connection con;
    private Statement st;
    private PreparedStatement pst;
    private ResultSet rs;
    private CallableStatement cs;


}
