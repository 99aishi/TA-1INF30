package pe.edu.pucp.economix.rrhh.test;

import pe.edu.pucp.economix.rrhh.boi.AreaBOImpl;
import pe.edu.pucp.economix.rrhh.ibo.IAreaBO;
import pe.edu.pucp.economix.rrhh.model.Area;

import java.util.List;

public class AreaTest {
    private static IAreaBO areaBO = new AreaBOImpl();
    public static List<Area> pruebaInsercion() throws Exception {


        Area areaGerenciaGeneral = new Area();
        areaGerenciaGeneral.setNombre("Gerencia General");
        areaGerenciaGeneral.setDescripcion("Administración de la empresa y su rumbo");
        areaGerenciaGeneral.setIdArea(areaBO.insertar(areaGerenciaGeneral));

        Area areaFinanzas = new Area();
        areaFinanzas.setNombre("Finanzas");
        areaFinanzas.setDescripcion("Gestión contable y tesorería");
        areaFinanzas.setIdArea(areaBO.insertar(areaFinanzas));

        Area areaTI = new Area();
        areaTI.setNombre("Tecnología (TI)");
        areaTI.setDescripcion("Infraestructura y Desarrollo");
        areaTI.setIdArea(areaBO.insertar(areaTI));

        List<Area> listaAreas = areaBO.listarTodas();
        for (Area a : listaAreas) {
            System.out.println(a);
        }
        System.out.println();
        return listaAreas;
    }

    public static Area buscarID(int idArea) throws Exception {
        return areaBO.buscarPorId(idArea);
    }
}
