package pe.edu.pucp.economix.config;

import java.sql.*;
import java.util.Map;
import java.util.ResourceBundle;

public class DBManager {
    // 1. ELIMINAMOS 'con' y 'rs' como variables globales de clase.
    private static DBManager dbManager;

    private final String hostname;
    private final String esquema;
    private final String puerto;
    private final String usuario;
    private final String password;
    private final String url;

    // 2. Para soportar transacciones por hilo de forma limpia, usamos ThreadLocal.
    // Esto asegura que cada hilo tenga su propia conexión transaccional sin mezclarse.
    private final ThreadLocal<Connection> conexionTransaccional = new ThreadLocal<>();
    private final ThreadLocal<Connection> conexionLectura = new ThreadLocal<>();

    private DBManager(){
        ResourceBundle db = ResourceBundle.getBundle("datosBD");
        this.hostname = db.getString("db.host");
        this.esquema = db.getString("db.esquema");
        this.puerto = db.getString("db.puerto");
        this.usuario = db.getString("db.usuario");
        this.password = db.getString("db.password");
        this.url = "jdbc:mysql://" + this.hostname
                + ":" + this.puerto + "/" + this.esquema + "?useSSL=FALSE";
    }

    // Doble verificación para asegurar que la inicialización del Singleton sea Thread-Safe
    public static DBManager getDBManager(){
        if(dbManager == null){
            synchronized (DBManager.class) {
                if(dbManager == null) {
                    dbManager = new DBManager();
                }
            }
        }
        return dbManager;
    }

    // Devuelve siempre una conexión nueva, aislada para el hilo que la solicita
    public Connection getConnection() throws Exception{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(this.url, this.usuario, this.password);
        }catch(Exception ex){
            System.out.println("Error al conectar con la base de datos: " + ex);
            throw ex;
        }
    }
    public void cerrarConexion() throws SQLException {
        // Cerrar conexión transaccional
        Connection con = conexionTransaccional.get();
        if (con != null) {
            try {
                if (!con.isClosed()) {
                    con.close();
                }
            } finally {
                conexionTransaccional.remove();
            }
        }
        // Cerrar conexión de lectura
        Connection conL = conexionLectura.get();
        if (conL != null) {
            try {
                if (!conL.isClosed()) {
                    conL.close();
                }
            } finally {
                conexionLectura.remove();
            }
        }
    }

    // ==========================================
    // MÓDULO DE TRANSACCIONES (Thread-Safe usando ThreadLocal)
    // ==========================================
    public void iniciarTransaccion() throws SQLException{
        try{
            Connection con = getConnection();
            con.setAutoCommit(false);
            conexionTransaccional.set(con); // Se guarda exclusivamente para ESTE hilo
        } catch (Exception e) {
            throw new SQLException("Error al iniciar transacción", e);
        }
    }

    public void cancelarTransaccion() throws SQLException{
        Connection con = conexionTransaccional.get();
        if (con != null) {
            try {
                con.rollback();
            } finally {
                con.close();
                conexionTransaccional.remove(); // Limpia el hilo
            }
        }
    }

    public void confirmarTransaccion() throws SQLException{
        Connection con = conexionTransaccional.get();
        if (con != null) {
            try {
                con.commit();
            } finally {
                con.close();
                conexionTransaccional.remove(); // Limpia el hilo
            }
        }
    }

    // ==========================================
    // FORMACIÓN DE PROCEDIMIENTOS (Pasando la conexión local)
    // ==========================================
    public CallableStatement formarLlamadaProcedimiento(Connection con, String nombreProcedimiento, Map<String, Object> parametrosEntrada, Map<String, Object> parametrosSalida) throws Exception {
        StringBuilder call = new StringBuilder("{call " + nombreProcedimiento + "(");
        int cantParametrosEntrada = (parametrosEntrada != null) ? parametrosEntrada.size() : 0;
        int cantParametrosSalida = (parametrosSalida != null) ? parametrosSalida.size() : 0;
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

    // ==========================================
    // EJECUCIÓN DE PROCEDIMIENTOS
    // ==========================================
    public int ejecutarProcedimiento(String nombreProcedimiento, Map<String, Object> parametrosEntrada, Map<String, Object> parametrosSalida) throws SQLException {
        if (conexionTransaccional.get() != null) {
            return ejecutarProcedimientoTransaccion(nombreProcedimiento, parametrosEntrada, parametrosSalida);
        }
        int resultado = 0;
        // La conexión se maneja dentro de este bloque localmente y se cerrará automáticamente al terminar
        try (Connection con = getConnection();
             CallableStatement cst = formarLlamadaProcedimiento(con, nombreProcedimiento, parametrosEntrada, parametrosSalida)) {

            if(parametrosEntrada != null) registrarParametrosEntrada(cst, parametrosEntrada);
            if(parametrosSalida != null) registrarParametrosSalida(cst, parametrosSalida);

            resultado = cst.executeUpdate();

            if(parametrosSalida != null) obtenerValoresSalida(cst, parametrosSalida);
        } catch (Exception e) {
            throw new SQLException(e);
        }
        return resultado;
    }

    // CRÍTICO: Este método ya no almacena el ResultSet en una propiedad de la clase.
    // Ojo: Quien consuma este ResultSet debe encargarse de cerrar la conexión asociada cuando termine de leerlo.
    public ResultSet ejecutarProcedimientoLectura(String nombreProcedimiento, Map<String,Object> parametrosEntrada) {
        try {
            Connection con = getConnection();
            conexionLectura.set(con);
            CallableStatement cs = formarLlamadaProcedimiento(con, nombreProcedimiento, parametrosEntrada, null);
            if(parametrosEntrada != null) {
                registrarParametrosEntrada(cs, parametrosEntrada);
            }
            // Devolvemos el ResultSet. Al cerrar este RS desde tu DAO, deberías cerrar también su Connection/Statement
            return cs.executeQuery();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Transaccional (Usa la conexión guardada en el ThreadLocal del hilo actual)
    public int ejecutarProcedimientoTransaccion(String nombreProcedimiento, Map<String, Object> parametrosEntrada, Map<String, Object> parametrosSalida) {
        int resultado = 0;
        Connection con = conexionTransaccional.get();
        if (con == null) {
            System.out.println("Error: No hay ninguna transacción activa en este hilo.");
            return 0;
        }
        try {
            StringBuilder call = new StringBuilder("{call " + nombreProcedimiento + "(");
            int cantParametrosEntrada = (parametrosEntrada != null) ? parametrosEntrada.size() : 0;
            int cantParametrosSalida = (parametrosSalida != null) ? parametrosSalida.size() : 0;
            int numParams =  cantParametrosEntrada + cantParametrosSalida;
            for (int i = 0; i < numParams; i++) {
                call.append("?");
                if (i < numParams - 1) call.append(",");
            }
            call.append(")}");

            try (CallableStatement cst = con.prepareCall(call.toString())) {
                if(parametrosEntrada != null) registrarParametrosEntrada(cst, parametrosEntrada);
                if(parametrosSalida != null) registrarParametrosSalida(cst, parametrosSalida);

                resultado = cst.executeUpdate();

                if(parametrosSalida != null) obtenerValoresSalida(cst, parametrosSalida);
            }
        } catch(SQLException ex){
            System.out.println("Error ejecutando procedimiento transaccional: " + ex.getMessage());
        }
        return resultado;
    }

    // Métodos auxiliares de registro (Se mantienen igual ya que operan sobre variables locales de los métodos)
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
                default -> {}
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
            }
            parametrosSalida.put(nombre, value);
        }
    }
}