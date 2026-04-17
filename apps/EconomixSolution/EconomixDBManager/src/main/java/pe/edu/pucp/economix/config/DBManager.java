package pe.edu.pucp.economix.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

public class DBManager {
    private static Connection connection;
    private static DBManager dbManager;

    private final String hostname;
    private final String esquema;
    private final String puerto;
    private final String usuario;
    private final String password;
    private final String url;

    //Constructores
    private DBManager(){
        //Asignamos los valores dentro de los atributos
        ResourceBundle db = ResourceBundle.getBundle("datosBD"); //Desde el archivo de resources del proyecto
        this.hostname = db.getString("db.host");
        this.esquema = db.getString("db.esquema");
        this.puerto = db.getString("db.puerto");
        this.usuario = db.getString("db.usuario");
        this.password = db.getString("db.password");
        this.url = "jdbc:mysql://" + this.hostname
                + ":" + this.puerto + "/" + this.esquema;
    }

    public Connection getConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(this.url, this.usuario, this.password);
        }catch(Exception ex){
            System.out.println("Error al conecntar con la base de datos" + ex);
        }
        return connection;
    }

    public static DBManager getDBManager(){
        //Lo debemos de inicializar
        if(dbManager == null){
            //No se inicializó
            dbManager = new DBManager();
        }
        return dbManager;
    }
}
