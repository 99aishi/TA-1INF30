package pe.edu.pucp.economix.rrhh.test;

import pe.edu.pucp.economix.rrhh.implement.EmpleadoImplement;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.rrhh.model.EstadoUsuario;
import pe.edu.pucp.economix.rrhh.model.Rol;

import java.util.List;

public class EmpleadoTest {
    public static List<Empleado> pruebaInsercion(Area areaGerenciaGeneral, Rol rolGerenteGeneral,
                                Area areaTI, Rol rolAnalista,
                                Area areaFinanzas, Rol rolGerente){
        EmpleadoImplement empleadoDAO = new EmpleadoImplement();

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
        return listaEmpleados;
    }
}
