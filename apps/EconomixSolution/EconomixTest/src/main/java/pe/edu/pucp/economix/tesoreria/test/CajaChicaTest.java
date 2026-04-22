package pe.edu.pucp.economix.tesoreria.test;

import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.tesoreria.implement.CajaChicaImplement;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;
import pe.edu.pucp.economix.tesoreria.model.EstadoFondo;

import java.util.Date;
import java.util.List;

public class CajaChicaTest {
    public static List<CajaChica> probarInserciones(Area areaTI){
        CajaChicaImplement cajachicaDAO=new CajaChicaImplement();
        Date fechaActual= new Date();
        CajaChica caja= new CajaChica("Prueba1",1000, EstadoFondo.Activo,fechaActual,2000,areaTI);
        
        caja.setMoneda(m);

    }
}
