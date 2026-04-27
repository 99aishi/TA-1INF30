package pe.edu.pucp.economix.main;

import java.util.List;

import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.ComprobantePago;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.operaciones.test.CicloCajaChicaTest;
import pe.edu.pucp.economix.operaciones.test.ComprobantePagoTest;
import pe.edu.pucp.economix.operaciones.test.SolicitudGastoTest;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.rrhh.model.Rol;
import pe.edu.pucp.economix.rrhh.test.AdministradorTest;
import pe.edu.pucp.economix.rrhh.test.AreaTest;
import pe.edu.pucp.economix.rrhh.test.EmpleadoTest;
import pe.edu.pucp.economix.rrhh.test.RolTest;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;
import pe.edu.pucp.economix.tesoreria.model.Moneda;
import pe.edu.pucp.economix.tesoreria.test.CajaChicaTest;
import pe.edu.pucp.economix.tesoreria.test.CuentaBancariaTest;
import pe.edu.pucp.economix.tesoreria.test.EntregaARendirTest;
import pe.edu.pucp.economix.tesoreria.test.MonedaTest;

public class Main {
    public static void main(String []args){

        //Recursos Humanos Testing
        List<Area> areas = AreaTest.pruebaInsercion();
        List<Rol> roles = RolTest.pruebaInsercion();
        AdministradorTest.pruebaInsercion();
        List<Empleado> empleados = EmpleadoTest.pruebaInsercion(areas.get(0), roles.get(0),
                                areas.get(1), roles.get(1),
                                areas.get(2), roles.get(2));

        System.out.println(EmpleadoTest.buscarID(empleados.get(2).getUsuarioID()));
        EmpleadoTest.eliminar(empleados.get(3).getUsuarioID());
        System.out.println(EmpleadoTest.buscarID(empleados.get(2).getUsuarioID()));

        //Tesorería Testing
        List<Moneda> monedas = MonedaTest.pruebaInsercion();
        CuentaBancariaTest.pruebaInsercion(empleados.get(1), monedas.get(0),
                empleados.get(2), monedas.get(1), areas.get(1));
        List<CajaChica>listaCajasChicas = CajaChicaTest.pruebaInsercion(areas.get(2), areas.get(1));
        EntregaARendirTest.pruebaInsercion(empleados.get(0), empleados.get(1), empleados.get(2));

        //Operaciones Testing
        // Ejemplo de cómo orquestarías los tests separados
        List<CicloCajaChica> ciclos = CicloCajaChicaTest.pruebaInsercion(listaCajasChicas.get(0), listaCajasChicas.get(1));
        List<SolicitudGasto> solicitudes = SolicitudGastoTest.pruebaInsercion(ciclos.get(0), empleados.get(1), empleados.get(2));
        List<ComprobantePago> facturas = ComprobantePagoTest.pruebaInsercion(solicitudes.getFirst(), monedas.getFirst());

    }

}
