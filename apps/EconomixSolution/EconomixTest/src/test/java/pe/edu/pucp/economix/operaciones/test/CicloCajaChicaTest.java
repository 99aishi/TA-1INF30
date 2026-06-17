package pe.edu.pucp.economix.operaciones.test;

import java.util.Date;
import java.util.List;

import pe.edu.pucp.economix.operaciones.boi.CicloCajaBOImpl;
import pe.edu.pucp.economix.operaciones.ibo.ICicloCajaBO;
import pe.edu.pucp.economix.operaciones.daoi.CicloCajaChicaDAOImpl;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoCicloCaja;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;

public class CicloCajaChicaTest {
    private static final CicloCajaChicaDAOImpl cicloDAO = new CicloCajaChicaDAOImpl();
    private static final ICicloCajaBO cicloCajaBO = new CicloCajaBOImpl();
    public static List<CicloCajaChica> pruebaInsercion(CajaChica cajaTI, CajaChica cajaFinanzas) throws Exception {
        CicloCajaChica cicloTI = new CicloCajaChica();
        cicloTI.setCajaChica(cajaTI);
        cicloTI.setEstado(EstadoCicloCaja.ABIERTO);
        cicloTI.setFechaApertura(new Date());
        cicloTI.setNumeroSemana(1);
        cicloTI.setSaldoInicial(cajaTI.getMontoTecho());
        cicloTI.setIdCicloCaja(cicloCajaBO.insertar(cicloTI, 1));

        // Ciclo 2: Para el área de Finanzas (Semana 1)
        CicloCajaChica cicloFinanzas = new CicloCajaChica();
        cicloFinanzas.setCajaChica(cajaFinanzas);
        cicloFinanzas.setEstado(EstadoCicloCaja.ABIERTO);
        cicloFinanzas.setFechaApertura(new Date());
        cicloFinanzas.setNumeroSemana(1);
        cicloFinanzas.setSaldoInicial(cajaFinanzas.getMontoTecho());
        cicloFinanzas.setIdCicloCaja(cicloCajaBO.insertar(cicloFinanzas, 1));

        // Listar y verificar
        List<CicloCajaChica> ciclos = cicloCajaBO.listarTodas();
        for (CicloCajaChica ciclo : ciclos) {
            System.out.println(ciclo);
        }
        System.out.println();
        return ciclos;

    }
}
