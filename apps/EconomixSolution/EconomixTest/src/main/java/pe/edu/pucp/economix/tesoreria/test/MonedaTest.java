package pe.edu.pucp.economix.tesoreria.test;

import java.util.List;

import pe.edu.pucp.economix.tesoreria.boi.MonedaBOImpl;
import pe.edu.pucp.economix.tesoreria.ibo.IMonedaBO;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

public class MonedaTest {
    public static List<Moneda> pruebaInsercion() throws Exception {
        IMonedaBO monedaBO= new MonedaBOImpl();

        Moneda dolar= new Moneda(0,"USD","$",
                "dolar americano",
                "moneda que sirve para realizar transacciones a cuentas bancarias internacionales", true);
        dolar.setIdMoneda(monedaBO.insertar(dolar));

        Moneda sol = new Moneda(1,"PEN", "S/.", "sol peruano",
                "moneda principal que sirve para realizar transacciones a cuentas bancarias nacionales", true);
        sol.setIdMoneda(monedaBO.insertar(sol));


        List<Moneda> monedas=monedaBO.listarTodas();
        for(Moneda mon : monedas){
            System.out.println(mon);
        }
        System.out.println();

        // testeamos el procedure de busqueda por codigo iso, simbolo o nombre
        String busqueda;
        busqueda = "olar";

        List<Moneda> monedas1 = monedaBO.listarMonedas_X_codigoISO_nombre_simbolo(busqueda);
        for(Moneda mon : monedas1){
            System.out.println(mon);
        }
        System.out.println();

        List<Moneda> monedas2 = monedaBO.listarMonedas_X_estado(true);
        for(Moneda mon : monedas2){
            System.out.println(mon);
        }
        System.out.println();
        return monedas;
    }
}
