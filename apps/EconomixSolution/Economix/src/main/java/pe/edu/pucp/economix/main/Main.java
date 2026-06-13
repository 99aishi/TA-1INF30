package pe.edu.pucp.economix.main;

import java.sql.SQLException;
import java.util.List;

import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.ComprobantePago;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.operaciones.test.CicloCajaChicaTest;
import pe.edu.pucp.economix.operaciones.test.ComprobantePagoTest;
import pe.edu.pucp.economix.operaciones.test.SolicitudGastoTest;
import pe.edu.pucp.economix.rrhh.boi.*;
import pe.edu.pucp.economix.rrhh.ibo.*;
import pe.edu.pucp.economix.rrhh.model.*;
import pe.edu.pucp.economix.rrhh.test.AdministradorTest;
import pe.edu.pucp.economix.rrhh.test.AreaTest;
import pe.edu.pucp.economix.rrhh.test.EmpleadoTest;
import pe.edu.pucp.economix.rrhh.test.RolTest;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;
import pe.edu.pucp.economix.tesoreria.model.Moneda;
import pe.edu.pucp.economix.tesoreria.test.CajaChicaTest;
import pe.edu.pucp.economix.tesoreria.test.CuentaBancariaTest;
import pe.edu.pucp.economix.tesoreria.test.MonedaTest;

public class Main {
    public static void main(String []args) throws Exception {

        //Recursos Humanos Testing
//        List<Area> areas = AreaTest.pruebaInsercion();
//        List<Rol> roles = RolTest.pruebaInsercion();
//        List<Empleado> empleados = EmpleadoTest.pruebaInsercion(areas.get(0), roles.get(0),
//                                areas.get(1), roles.get(1),
//                                areas.get(2), roles.get(2));
//        AdministradorTest.pruebaInsercion();
//
//        System.out.println(EmpleadoTest.buscarID(1));
//        EmpleadoTest.eliminar(1);
//        System.out.println(EmpleadoTest.buscarID(1));
//
//        //Tesorería Testing
//        List<Moneda> monedas = MonedaTest.pruebaInsercion();
//        CuentaBancariaTest.pruebaInsercion(empleados.get(1), monedas.get(0),
//                empleados.get(2), monedas.get(1), areas.get(1));
//        List<CajaChica>listaCajasChicas = CajaChicaTest.pruebaInsercion(areas.get(2), areas.get(1));
////
////        //Operaciones Testing
////        // Ejemplo de cómo orquestarías los tests separados
//        List<CicloCajaChica> ciclos = CicloCajaChicaTest.pruebaInsercion(listaCajasChicas.get(0), listaCajasChicas.get(1));
//        List<SolicitudGasto> solicitudes = SolicitudGastoTest.pruebaInsercion(ciclos.get(0), empleados.get(1), empleados.get(2));
//        List<ComprobantePago> facturas = ComprobantePagoTest.pruebaInsercion(solicitudes.getFirst(), monedas.getFirst());
//        facturas.getFirst();
//
//        EmpleadoTest.probarLoginEmpleado("p.paramo@acervo.com", "TILIN");
//        AdministradorTest.probarLoginAdministrador("soporte@empresa.com", "TILIN");
//        EmpleadoTest.probarListarPorNombreApellido("Pe");


//        Administrador admin = new Administrador();
//        admin.setNombres("Administrador");
//        admin.setCorreo("administrador@economix.pe");
//        admin.setApellidoPaterno("Principal");
//        admin.setPassword("administrador123");
//
//        IAdministradorBO administradorBO = new AdministradorBOImpl();
//        administradorBO.insertar(admin);

        //IUsuarioBO usuarioBO = new UsuarioBOImpl();
        //Usuario logeado = usuarioBO.validarUsuario("administrador@economix.pe", "administrador123");

        //REtorna excepcion
        // Usuario fallido = usuarioBO.validarUsuario("administrador@economix.pe", "administrador1234");

        //System.out.println(logeado);
        //System.out.println(fallido);

        Rol rol = new Rol("PrimerArea", "Area encargada de ser la primera");
        IRolBO rolBO = new RolBOImpl();
        rol.setRolID(1);

        Area area = new Area();
        area.setNombre("Area1");
        area.setDescripcion("Descripcion del area");

            System.out.println(area);

        IAreaBO areaBO = new AreaBOImpl();
        area.setIdArea(1);

        Empleado empleado = new Empleado();
        empleado.setCorreo("empleado1@economix.pe");
        empleado.setPassword("empleado123");
        empleado.setNombres("Empleado");
        empleado.setApellidoPaterno("Primero");
        empleado.setApellidoMaterno("Primero");
        empleado.setArea(area);
        empleado.setRol(rol);
        empleado.setNumeroCelular("999999999");


        IEmpleadoBO empleadoBO = new EmpleadoBOImpl();
        System.out.println(empleadoBO.insertar(empleado));



    }

}
