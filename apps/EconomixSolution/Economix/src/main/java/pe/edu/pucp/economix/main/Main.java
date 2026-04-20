package pe.edu.pucp.economix.main;

import pe.edu.pucp.economix.operaciones.implement.RendicionImplement;
import pe.edu.pucp.economix.operaciones.model.EstadoRendicion;
import pe.edu.pucp.economix.operaciones.model.Rendicion;
import pe.edu.pucp.economix.rrhh.implement.*;
import pe.edu.pucp.economix.rrhh.model.*;
import pe.edu.pucp.economix.tesoreria.dao.IMonedaDAO;
import pe.edu.pucp.economix.tesoreria.implement.MonedaImplement;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import javax.naming.ldap.ControlFactory;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String []args){
//        Moneda moneda=new Moneda("PEN","S./",fechaCreacion,fechaModificacion)
//        //IMonedaDAO monedaDAO = new MonedaImplement();
//        Moneda moneda= new Moneda("USD","$");
//        IMonedaDAO monedaDAO=new MonedaImplement();
//        int resultado=monedaDAO.insertar(moneda);
//        if(resultado!=0){
//            System.out.println("Se inserto correctamente");
//        }

//        moneda.setIdMoneda(1);
//        moneda.setCodigoISO("PEN");
//        moneda.setSimbolo("S/.");
//        resultado=monedaDAO.modificar(moneda);
//        if(resultado!=0){
//            System.out.println("Se modifico correctamente");
//        }

//        Moneda moneda=monedaDAO.buscarPorId(2);
//        System.out.println(moneda);
//

//        List<Moneda> monedas=monedaDAO.listarTodas();
//        for(int i=0;i<monedas.size();i++){
//            System.out.println(monedas.get(i));
//        }


        /* RRHH Pruebas
        AreaImplement areaDAO = new AreaImplement();
        RolImplement rolDAO = new RolImplement();
        EmpleadoImplement empleadoDAO = new EmpleadoImplement();
        AdministradorImplement adminDAO = new AdministradorImplement();


        // 1. Area y Rol
        Area areaTI = new Area("Tecnología (TI)", "Infraestructura y Desarrollo");
        areaTI.setIdArea(areaDAO.insertar(areaTI));
        Area areaFinanzas = new Area("Finanzas", "Gestión contable y tesorería");
        areaFinanzas.setIdArea(areaDAO.insertar(areaFinanzas));
        Area areaGerenciaGeneral = new Area("Gerencia General", "Administración de la empresa y su rumbo");
        areaGerenciaGeneral.setIdArea(areaDAO.insertar(areaGerenciaGeneral));

        List<Area> listaAreas = areaDAO.listarTodas();
        for (Area a : listaAreas) {
            System.out.println(a);
        }
        System.out.println();

        Rol rolGerenteGeneral = new Rol("Gerente General", "Responsable de la empresa");
        rolGerenteGeneral.setRolID(rolDAO.insertar(rolGerenteGeneral));
        Rol rolGerente = new Rol("Gerente de Área", "Responsable de departamento");
        rolGerente.setRolID(rolDAO.insertar(rolGerente));
        Rol rolAnalista = new Rol("Analista Senior", "Especialista con experiencia");
        rolAnalista.setRolID(rolDAO.insertar(rolAnalista));

        List<Rol> listaRoles = rolDAO.listarTodas();
        for (Rol r : listaRoles) {
            System.out.println(r);
        }
        System.out.println();

        // 2. Jefe
        Empleado jefe = new Empleado();
        jefe.setNombre("Pedro");
        jefe.setApellidoPaterno("Páramo");
        jefe.setEstado(EstadoUsuario.Activo);
        jefe.setCorreoInstitucional("p.paramo@acervo.com");
        jefe.setPassword("TILIN");
        jefe.setArea(areaGerenciaGeneral);
        jefe.setRol(rolGerenteGeneral);
        jefe.setJefeDirecto(null);
        jefe.setUsuarioID(empleadoDAO.insertar(jefe));

        // Empleado 1: Augusto Pérez (TI)
        Empleado emp1 = new Empleado();
        emp1.setNombre("Augusto");
        emp1.setApellidoPaterno("Pérez");
        emp1.setApellidoMaterno("Niebla");
        emp1.setEstado(EstadoUsuario.Activo);
        emp1.setCorreoInstitucional("a.perez@acervo.com");
        emp1.setPassword("TILIN");
        emp1.setNumeroCelular("993000003");
        emp1.setArea(areaTI);
        emp1.setRol(rolAnalista);
        emp1.setJefeDirecto(jefe);
        emp1.setUsuarioID(empleadoDAO.insertar(emp1));

        // Empleado 2: Esteban Trueba (Gerente de Finanzas)
        Empleado emp2 = new Empleado();
        emp2.setNombre("Esteban");
        emp2.setApellidoPaterno("Trueba");
        emp2.setApellidoMaterno("Del Valle");
        emp2.setEstado(EstadoUsuario.Activo);
        emp2.setCorreoInstitucional("e.trueba@acervo.com");
        emp2.setPassword("TILIN");
        emp2.setNumeroCelular("999000222");
        emp2.setArea(areaFinanzas);
        emp2.setRol(rolGerente);
        emp2.setJefeDirecto(jefe);
        emp2.setUsuarioID(empleadoDAO.insertar(emp2));

        // Empleado 3: Santiago Nasar (Analista en Finanzas bajo Esteban)
        Empleado emp3 = new Empleado();
        emp3.setNombre("Santiago");
        emp3.setApellidoPaterno("Nasar");
        emp3.setApellidoMaterno("Linares");
        emp3.setEstado(EstadoUsuario.Activo);
        emp3.setCorreoInstitucional("s.nasar@acervo.com");
        emp3.setPassword("TILIN");
        emp3.setNumeroCelular("991000001");
        emp3.setArea(areaFinanzas);
        emp3.setRol(rolAnalista);
        emp3.setJefeDirecto(emp2); // Su jefe es Esteban
        emp3.setUsuarioID(empleadoDAO.insertar(emp3));


        List<Empleado> listaEmpleados = empleadoDAO.listarTodas();
        for (Empleado e : listaEmpleados) {
            // Obtenemos los nombres de los objetos relacionados (Agregación)
            String nombreArea = (e.getArea() != null) ? e.getArea().getNombre() : "N/A";
            String tituloRol = (e.getRol() != null) ? e.getRol().getTitulo() : "N/A";

            System.out.println(String.format("ID: %d | Empleado: %-25s | Correo: %-25s | Área: %-15s | Rol: %s",
                    e.getUsuarioID(),
                    e.getNombre() + " " + e.getApellidoPaterno(),
                    e.getCorreoInstitucional(),
                    nombreArea,
                    tituloRol));
        }
        System.out.println();
        // 4. Administradores
        // Administrador 1: Soporte Técnico Principal
        Administrador admin1 = new Administrador();
        admin1.setNombre("Soporte");
        admin1.setApellidoPaterno("Técnico");
        admin1.setCorreoSoporte("soporte@empresa.com");
        admin1.setPassword("TILIN");
        admin1.setEstado(EstadoUsuario.Activo);
        admin1.setUsuarioID(adminDAO.insertar(admin1));

        List<Administrador> listaAdmins = adminDAO.listarTodas();
        for (Administrador ad : listaAdmins) {
            // Obtenemos los nombres de los objetos relacionados (Agregación)

            System.out.println(String.format("ID: %d | Empleado: %-25s | Correo: %-25s",
                    ad.getUsuarioID(),
                    ad.getNombre() + " " + ad.getApellidoPaterno(),
                    ad.getCorreoSoporte()));
        }
        */

        /* Operaciones Pruebas*/
        RendicionImplement rendicionDAO = new RendicionImplement();

        Rendicion r1 = new Rendicion();
        r1.setFechaPresentacion(new Date());
        r1.setTotalDeclarado(1500.00);
        r1.setEstado(EstadoRendicion.EnEspera);
        r1.setComentario("Rendición de caja chica Finanzas - Semana 1");
        r1.setIdRendicion(rendicionDAO.insertar(r1));

        Rendicion r2 = new Rendicion();
        r2.setFechaPresentacion(new Date());
        r2.setTotalDeclarado(850.00);
        r2.setEstado(EstadoRendicion.EnEspera);
        r2.setComentario("Viáticos viaje comercial Norte");
        r2.setIdRendicion(rendicionDAO.insertar(r2));




    }

}
