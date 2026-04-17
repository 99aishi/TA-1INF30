package pe.edu.pucp.economix.main;

import pe.edu.pucp.economix.config.DBManager;

import java.sql.Connection;

public class Main {
    public static void main(String []args){
        try {
            Connection con = DBManager.getDBManager().getConnection();


        }catch (Exception ex){
            System.out.println(ex);
        }

    }

}
