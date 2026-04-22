package pe.edu.pucp.economix.rrhh.test;

import pe.edu.pucp.economix.rrhh.implement.AreaImplement;
import pe.edu.pucp.economix.rrhh.model.Area;

import java.util.List;

public class AreaTest {
    private static AreaImplement areaDAO = new AreaImplement();
    public static List<Area> pruebaInsercion(){

        Area areaGerenciaGeneral = new Area("Gerencia General", "Administración de la empresa y su rumbo");
        areaGerenciaGeneral.setIdArea(areaDAO.insertar(areaGerenciaGeneral));

        Area areaFinanzas = new Area("Finanzas", "Gestión contable y tesorería");
        areaFinanzas.setIdArea(areaDAO.insertar(areaFinanzas));

        Area areaTI = new Area("Tecnología (TI)", "Infraestructura y Desarrollo");
        areaTI.setIdArea(areaDAO.insertar(areaTI));

        List<Area> listaAreas = areaDAO.listarTodas();
        for (Area a : listaAreas) {
            System.out.println(a);
        }
        System.out.println();
        return listaAreas;
    }

    public static Area buscarID(int idArea){
        return areaDAO.buscarPorId(idArea);
    }
}
