package pe.edu.pucp.economix.tesoreria.test;

import java.util.List;

import pe.edu.pucp.economix.tesoreria.boi.MonedaBOImpl;
import pe.edu.pucp.economix.tesoreria.ibo.IMonedaBO;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

public class MonedaTest {
    public static List<Moneda> pruebaInsercion() throws Exception {
        IMonedaBO monedaBO= new MonedaBOImpl();

        Moneda dolar= new Moneda(0,"USD","$");
        dolar.setIdMoneda(monedaBO.insertar(dolar));

        Moneda sol = new Moneda(0,"PEN", "S/.");
        sol.setIdMoneda(monedaBO.insertar(sol));


        List<Moneda> monedas=monedaBO.listarTodas();
        for(Moneda mon : monedas){
            System.out.println(mon);
        }
        System.out.println();
        return monedas;
    }
}
