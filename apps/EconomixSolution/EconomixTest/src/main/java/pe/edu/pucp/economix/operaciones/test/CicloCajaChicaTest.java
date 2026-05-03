package pe.edu.pucp.economix.operaciones.test;

import java.sql.SQLException;
import java.util.List;

import pe.edu.pucp.economix.operaciones.implement.CicloCajaChicaImplement;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;

public class CicloCajaChicaTest {
    private static final CicloCajaChicaImplement cicloDAO = new CicloCajaChicaImplement();
    public static List<CicloCajaChica> pruebaInsercion(CajaChica cajaTI, CajaChica cajaFinanzas) throws SQLException{
        CicloCajaChica cicloTI = new CicloCajaChica(

        );
        cicloTI.setIdCicloCaja(cicloDAO.insertar(cicloTI));

        // Ciclo 2: Para el área de Finanzas (Semana 1)
        CicloCajaChica cicloFinanzas = new CicloCajaChica(

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
