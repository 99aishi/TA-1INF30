package pe.edu.pucp.economix.tesoreria.test;

import java.util.List;

import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.tesoreria.boi.CajaChicaBOImpl;
import pe.edu.pucp.economix.tesoreria.ibo.ICajaChicaBO;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;
import pe.edu.pucp.economix.tesoreria.model.EstadoFondo;

public class CajaChicaTest {
    private final static ICajaChicaBO cajaChicaBO = new CajaChicaBOImpl();

    public static List<CajaChica> pruebaInsercion(Area areaTI, Area areaFinanzas) throws Exception {
        CajaChica cajaChicaTI = new CajaChica();
        cajaChicaTI.setAreaAsignada(areaTI);
        cajaChicaTI.setNombre("Caja Chica TI");
        cajaChicaTI.setMontoTecho(1000.00);
        cajaChicaTI.setEstado(EstadoFondo.ACTIVO);
        cajaChicaTI.setIdFondo(cajaChicaBO.insertar(cajaChicaTI, 1));

        CajaChica cajaChicaFinanzas = new CajaChica();
        cajaChicaFinanzas.setAreaAsignada(areaFinanzas);
        cajaChicaFinanzas.setNombre("Caja Chica Finanzas");
        cajaChicaFinanzas.setMontoTecho(1000.00);
        cajaChicaFinanzas.setEstado(EstadoFondo.ACTIVO);
        cajaChicaTI.setIdFondo(cajaChicaBO.insertar(cajaChicaFinanzas, 1));

        List<CajaChica> listaCajasChicas = cajaChicaBO.listarTodas();
        for (CajaChica cajaChica : listaCajasChicas) {
            System.out.println(cajaChica);
        }
        System.out.println();
        return listaCajasChicas;
    }
}
