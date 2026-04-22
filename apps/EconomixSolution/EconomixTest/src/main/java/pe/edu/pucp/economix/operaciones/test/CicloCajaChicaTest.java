package pe.edu.pucp.economix.operaciones.test;

import pe.edu.pucp.economix.operaciones.implement.CicloCajaChicaImplement;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;

import java.util.Date;
import java.util.List;

public class CicloCajaChicaTest {
    private static CicloCajaChicaImplement cicloDAO = new CicloCajaChicaImplement();
    public static List<CicloCajaChica> pruebaInsercion(CajaChica cajaTI, CajaChica cajaFinanzas){
        CicloCajaChica cicloTI = new CicloCajaChica(
                1,
                new Date(),
                null, // Aún no cierra
                cajaTI.getMontoTecho(),
                cajaTI
        );
        cicloTI.setIdCicloCaja(cicloDAO.insertar(cicloTI));

        // Ciclo 2: Para el área de Finanzas (Semana 1)
        CicloCajaChica cicloFinanzas = new CicloCajaChica(
                1,
                new Date(),
                null,
                cajaFinanzas.getMontoTecho(),
                cajaFinanzas
        );
        cicloFinanzas.setIdCicloCaja(cicloDAO.insertar(cicloFinanzas));

        // Listar y verificar
        List<CicloCajaChica> ciclos = cicloDAO.listarTodas();
        for (CicloCajaChica ciclo : ciclos) {
            System.out.println(ciclo);
        }
        System.out.println();
        return ciclos;

    }
}
