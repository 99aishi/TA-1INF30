package pe.edu.pucp.economix.tesoreria.test;

import pe.edu.pucp.economix.tesoreria.implement.MonedaImplement;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.util.List;

public class MonedaTest {
    public static List<Moneda> pruebaInsercion(){
        MonedaImplement monedaDAO = new MonedaImplement();

        Moneda dolar= new Moneda("USD","$");
        dolar.setIdMoneda(monedaDAO.insertar(dolar));

        Moneda sol = new Moneda("PEN", "S/.");
        sol.setIdMoneda(monedaDAO.insertar(sol));


        List<Moneda> monedas=monedaDAO.listarTodas();
        for(Moneda mon : monedas){
            System.out.println(mon);
        }
        return monedas;
    }
}
