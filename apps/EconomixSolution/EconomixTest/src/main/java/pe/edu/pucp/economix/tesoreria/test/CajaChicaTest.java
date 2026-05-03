package pe.edu.pucp.economix.tesoreria.test;

import java.sql.SQLException;
import java.util.List;

import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.tesoreria.implement.CajaChicaImplement;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;
import pe.edu.pucp.economix.tesoreria.model.EstadoFondo;

public class CajaChicaTest {
    private final static CajaChicaImplement cajaChicaDAO = new CajaChicaImplement();

    public static List<CajaChica> pruebaInsercion(Area areaTI, Area areaFinanzas) throws SQLException {
        CajaChica cajaChicaTI = new CajaChica();
        cajaChicaTI.setAreaAsignada(areaTI);
        cajaChicaTI.setNombre("Caja Chica TI");
        cajaChicaTI.setMontoTecho(1.00);
        cajaChicaTI.setEstado(EstadoFondo.Activo);
        cajaChicaTI.setIdFondo(cajaChicaDAO.insertar(cajaChicaTI));

        CajaChica cajaChicaFinanzas = new CajaChica();
        cajaChicaFinanzas.setAreaAsignada(areaFinanzas);
        cajaChicaFinanzas.setNombre("Caja Chica Finanzas");
        cajaChicaFinanzas.setMontoTecho(1000.00);
        cajaChicaFinanzas.setEstado(EstadoFondo.Activo);
        cajaChicaTI.setIdFondo(cajaChicaDAO.insertar(cajaChicaFinanzas));

        List<CajaChica> listaCajasChicas = cajaChicaDAO.listarTodas();
        for (CajaChica cajaChica : listaCajasChicas) {
            System.out.println(cajaChica);
        }
        System.out.println();
        return listaCajasChicas;
    }
}
