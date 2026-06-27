package pe.edu.pucp.economix.config;

import java.sql.*;
import java.util.Map;
import java.util.ResourceBundle;

public class DBManager {
    private static Connection con;
    private static DBManager dbManager;

    private final String hostname;
    private final String esquema;
    private final String puerto;
    private final String usuario;
    private final String password;
    private final String url;

    private ResultSet rs;

    //CONSTRUCTORES
    private DBManager(){
        //Asignamos los valores dentro de los atributos
        ResourceBundle db = ResourceBundle.getBundle("datosBD"); //Desde el archivo de resources del proyecto
        this.hostname = db.getString("db.host");
        this.esquema = db.getString("db.esquema");
        this.puerto = db.getString("db.puerto");
        this.usuario = db.getString("db.usuario");
        this.password = db.getString("db.password");
        this.url = "jdbc:mysql://" + this.hostname
                + ":" + this.puerto + "/" + this.esquema + "?useSSL=FALSE";
    }
    public static DBManager getDBManager(){
        //Lo debemos de inicializar
        if(dbManager == null){
            //No se inicializó
            dbManager = new DBManager();
        }
        return dbManager;
    }
    //CONEXIÓN
    public Connection getConnection() throws Exception{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(this.url, this.usuario, this.password);
        }catch(Exception ex){
            System.out.println("Error al conectar con la base de datos" + ex);
            throw ex;
        }
        return con;
    }
    public void cerrarConexion() throws SQLException{
        try{
            con.close();
        }catch(SQLException ex){
            System.out.println("Error al cerrar la conexión: " + ex.getMessage());
            throw ex;
        }
    }
    //Transacciones
    public void iniciarTransaccion() throws SQLException{
        try{
            con = getConnection();
            con.setAutoCommit(false);
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
            throw ex;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void cancelarTransaccion() throws SQLException{
        try{
            con.rollback();
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
            throw ex;
        }finally{
            cerrarConexion();
        }
    }
    public void confirmarTransaccion() throws SQLException{
        try{
            con.commit();
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
            throw ex;
        }finally{
            cerrarConexion();
        }
    }

    //PROCEDIMIENTOS
    //FORMACIÓN
    public CallableStatement formarLlamadaProcedimiento(String nombreProcedimiento, Map<String, Object> parametrosEntrada, Map<String, Object> parametrosSalida) throws Exception {
        if (con == null || con.isClosed()) {
            con = getConnection();
        }
        StringBuilder call = new StringBuilder("{call " + nombreProcedimiento + "(");
        int cantParametrosEntrada = 0;
        int cantParametrosSalida = 0;
        if(parametrosEntrada!=null) cantParametrosEntrada = parametrosEntrada.size();
        if(parametrosSalida!=null) cantParametrosSalida = parametrosSalida.size();
        int numParams =  cantParametrosEntrada + cantParametrosSalida;
        for (int i = 0; i < numParams; i++) {
            call.append("?");
            if (i < numParams - 1) {
                call.append(",");
            }
        }
        call.append(")}");
        return con.prepareCall(call.toString());
    }
    private void registrarParametrosEntrada(CallableStatement cs, Map<String, Object> parametros) throws SQLException {
        for (Map.Entry<String, Object> entry : parametros.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value == null) {
                cs.setNull(key, java.sql.Types.NULL);
                continue;
            }
            switch (value) {
                case Integer entero -> cs.setInt(key, entero);
                case String cadena -> cs.setString(key, cadena);
                case Double decimal -> cs.setDouble(key, decimal);
                case Boolean booleano -> cs.setBoolean(key, booleano);
                case java.util.Date fecha -> cs.setDate(key, new java.sql.Date(fecha.getTime()));
                case Character caracter -> cs.setString(key, String.valueOf(caracter));
                case byte[] archivo -> cs.setBytes(key, archivo);
                default -> {
                }
                // Agregar más tipos según sea necesario
            }
        }
    }
    private void registrarParametrosSalida(CallableStatement cst, Map<String, Object> params) throws SQLException {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String posicion = entry.getKey();
            int sqlType = (int) entry.getValue();
            cst.registerOutParameter(posicion, sqlType);
        }
    }
    private void obtenerValoresSalida(CallableStatement cst, Map<String, Object> parametrosSalida) throws SQLException {
        for (Map.Entry<String, Object> entry : parametrosSalida.entrySet()) {
            String nombre = entry.getKey();
            int sqlType = (int) entry.getValue();
            Object value = null;
            switch (sqlType) {
                case Types.INTEGER -> value = cst.getInt(nombre);
                case Types.VARCHAR -> value = cst.getString(nombre);
                case Types.DOUBLE -> value = cst.getDouble(nombre);
                case Types.BOOLEAN -> value = cst.getBoolean(nombre);
                case Types.DATE -> value = cst.getDate(nombre);
                case Types.BLOB -> value = cst.getBytes(nombre);
                // Agregar más tipos según sea necesario
            }
            parametrosSalida.put(nombre, value);
        }
    }
    //Transaccional
    public CallableStatement formarLlamadaProcedimientoTransaccion(String nombreProcedimiento, Map<String, Object> parametrosEntrada, Map<String, Object> parametrosSalida) throws SQLException{
        StringBuilder call = new StringBuilder("{call " + nombreProcedimiento + "(");
        int cantParametrosEntrada = 0;
        int cantParametrosSalida = 0;
        if(parametrosEntrada!=null) cantParametrosEntrada = parametrosEntrada.size();
        if(parametrosSalida!=null) cantParametrosSalida = parametrosSalida.size();
        int numParams =  cantParametrosEntrada + cantParametrosSalida;
        for (int i = 0; i < numParams; i++) {
            call.append("?");
            if (i < numParams - 1) {
                call.append(",");
            }
        }
        call.append(")}");
        return con.prepareCall(call.toString());
    }
    //EJECUCIÓN
    public int ejecutarProcedimiento(String nombreProcedimiento, Map<String, Object> parametrosEntrada, Map<String, Object> parametrosSalida) throws SQLException {
        int resultado = 0;
        try{
            CallableStatement cst = formarLlamadaProcedimiento(nombreProcedimiento, parametrosEntrada, parametrosSalida);
            if(parametrosEntrada != null)
                registrarParametrosEntrada(cst, parametrosEntrada);
            if(parametrosSalida != null)
                registrarParametrosSalida(cst, parametrosSalida);

            resultado = cst.executeUpdate();

            if(parametrosSalida != null)
                obtenerValoresSalida(cst, parametrosSalida);
        } catch(SQLException ex){
            throw ex;
        } catch (Exception e) {
            throw new SQLException(e);
        } finally{
            if (con != null && con.getAutoCommit()) {
                cerrarConexion();
            }
        }
        return resultado;
    }
    public ResultSet ejecutarProcedimientoLectura(String nombreProcedimiento, Map<String,Object> parametrosEntrada){
        try{
            CallableStatement cs = formarLlamadaProcedimiento(nombreProcedimiento, parametrosEntrada, null);
            if(parametrosEntrada!=null)
                registrarParametrosEntrada(cs,parametrosEntrada);
            rs = cs.executeQuery();
        }catch(SQLException ex){
            System.out.println("Error ejecutando procedimiento almacenado de lectura: " + ex.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return rs;
    }
    //Transaccional
    public int ejecutarProcedimientoTransaccion(String nombreProcedimiento, Map<String, Object> parametrosEntrada, Map<String, Object> parametrosSalida) {
        int resultado = 0;
        try{
            CallableStatement cst = formarLlamadaProcedimientoTransaccion(nombreProcedimiento, parametrosEntrada, parametrosSalida);
            if(parametrosEntrada != null)
                registrarParametrosEntrada(cst, parametrosEntrada);
            if(parametrosSalida != null)
                registrarParametrosSalida(cst, parametrosSalida);

            resultado = cst.executeUpdate();

            if(parametrosSalida != null)
                obtenerValoresSalida(cst, parametrosSalida);
        }catch(SQLException ex){
            System.out.println("Error ejecutando procedimiento almacenado: " + ex.getMessage());
        }
        return resultado;
    }
}
