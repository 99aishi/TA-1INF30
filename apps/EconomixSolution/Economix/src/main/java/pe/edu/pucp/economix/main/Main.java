package pe.edu.pucp.economix.main;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.tesoreria.model.Moneda;
import pe.edu.pucp.economix.tesoreria.dao.IMonedaDAO;
import pe.edu.pucp.economix.tesoreria.implement.MonedaImplement;
import java.util.Date;

import java.sql.Connection;
import java.util.List;

public class Main {
    public static void main(String []args){
//        Date fechaCreacion=new Date();
//        Date fechaModificacion=new Date();
//        Moneda moneda=new Moneda("PEN","S./",fechaCreacion,fechaModificacion);
        IMonedaDAO monedaDAO=new MonedaImplement();
//        int resultado=monedaDAO.insertar(moneda);
//        if(resultado!=0){
//            System.out.println("Se inserto correctamente");
//        }
//        Date fechaModificacion=new Date();
//        Moneda moneda=new Moneda();
//        moneda.setIdMoneda(1);
//        moneda.setFechaModificacion(fechaModificacion);
//        moneda.setCodigoISO("USD");
//        moneda.setSimbolo("$");
//        int resultado=monedaDAO.modificar(moneda);
//        if(resultado!=0){
//            System.out.println("Se modifico correctamente");
//        }
//        Moneda moneda=monedaDAO.buscarPorId(1);
//        System.out.println(moneda);
        List<Moneda> monedas=monedaDAO.listarTodas();
        for(int i=0;i<monedas.size();i++){
            System.out.println(monedas.get(i));
        }





    }

}
