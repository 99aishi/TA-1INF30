package pe.edu.pucp.economix.tesoreria.test;

import java.sql.SQLException;
import java.util.List;

import pe.edu.pucp.economix.tesoreria.implement.MonedaImplement;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

public class MonedaTest {
    public static List<Moneda> pruebaInsercion() throws SQLException {
        MonedaImplement monedaDAO = new MonedaImplement();

        Moneda dolar= new Moneda(0,"USD","$");
        dolar.setIdMoneda(monedaDAO.insertar(dolar));

        Moneda sol = new Moneda(0,"PEN", "S/.");
        sol.setIdMoneda(monedaDAO.insertar(sol));


        List<Moneda> monedas=monedaDAO.listarTodas();
        for(Moneda mon : monedas){
            System.out.println(mon);
        }
        return monedas;
    }
}
