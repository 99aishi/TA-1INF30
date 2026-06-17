package pe.edu.pucp.economix.rrhh.test;

import pe.edu.pucp.economix.rrhh.boi.EmpleadoBOImpl;
import pe.edu.pucp.economix.rrhh.ibo.IEmpleadoBO;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.rrhh.model.EstadoUsuario;
import pe.edu.pucp.economix.rrhh.model.Rol;

import java.util.List;

public class EmpleadoTest {
    private static IEmpleadoBO empleadoBO = new EmpleadoBOImpl();
    public static List<Empleado> pruebaInsercion(Area areaGerenciaGeneral, Rol rolGerenteGeneral,
                                Area areaTI, Rol rolAnalista,
                                Area areaFinanzas, Rol rolGerente) throws Exception {

        // 2. Jefe
        Empleado jefe = new Empleado();
        jefe.setNombres("Pedro");
        jefe.setApellidoPaterno("Páramo");
        jefe.setEstado(EstadoUsuario.ACTIVO);
        jefe.setApellidoMaterno("Galvez");
        jefe.setCorreo("p.paramo@acervo.com");
        jefe.setPassword("TILIN");
        jefe.setArea(areaGerenciaGeneral);
        jefe.setRol(rolGerenteGeneral);
        jefe.setJefeDirecto(null);
        jefe.setNumeroCelular("993000093");
        jefe.setUsuarioID(empleadoBO.insertar(jefe, 1));

        // Empleado 1: Augusto Pérez (TI)
        Empleado emp1 = new Empleado();
        emp1.setNombres("Augusto");
        emp1.setApellidoPaterno("Pérez");
        emp1.setApellidoMaterno("Niebla");
        emp1.setEstado(EstadoUsuario.ACTIVO);
        emp1.setCorreo("a.perez@acervo.com");
        emp1.setPassword("TILIN");
        emp1.setNumeroCelular("993000003");
        emp1.setArea(areaTI);
        emp1.setRol(rolAnalista);
        emp1.setJefeDirecto(jefe);
        emp1.setUsuarioID(empleadoBO.insertar(emp1, 1));

        // Empleado 2: Esteban Trueba (Gerente de Finanzas)
        Empleado emp2 = new Empleado();
        emp2.setNombres("Esteban");
        emp2.setApellidoPaterno("Trueba");
        emp2.setApellidoMaterno("Del Valle");
        emp2.setEstado(EstadoUsuario.ACTIVO);
        emp2.setCorreo("e.trueba@acervo.com");
        emp2.setPassword("TILIN");
        emp2.setNumeroCelular("999000222");
        emp2.setArea(areaFinanzas);
        emp2.setRol(rolGerente);
        emp2.setJefeDirecto(jefe);
        emp2.setUsuarioID(empleadoBO.insertar(emp2, 1));

        // Empleado 3: Santiago Nasar (Analista en Finanzas bajo Esteban)
        Empleado emp3 = new Empleado();
        emp3.setNombres("Santiago");
        emp3.setApellidoPaterno("Nasar");
        emp3.setApellidoMaterno("Linares");
        emp3.setEstado(EstadoUsuario.ACTIVO);
        emp3.setCorreo("s.nasar@acervo.com");
        emp3.setPassword("TILIN");
        emp3.setNumeroCelular("991000001");
        emp3.setArea(areaFinanzas);
        emp3.setRol(rolAnalista);
        emp3.setJefeDirecto(emp2); // Su jefe es Esteban
        emp3.setUsuarioID(empleadoBO.insertar(emp3, 1));


        List<Empleado> listaEmpleados = empleadoBO.listarTodas();
        for (Empleado e : listaEmpleados) {
            // Obtenemos los nombres de los objetos relacionados (Agregación)
            System.out.println(e);
        }
        System.out.println();
        return listaEmpleados;
    }

    public static Empleado buscarID(int idEmpleado) throws Exception{
        return empleadoBO.buscarPorId(idEmpleado);
    }

    public static int eliminar(int id) throws Exception {
        return empleadoBO.eliminar(id, 1);
    }
    public static void probarListarPorNombreApellido(String busqueda) throws Exception {
        List<Empleado> empleados = empleadoBO.listarPorNombreApellido(busqueda);

        System.out.println("==============================================================================================================");
        System.out.println("                                      BÚSQUEDA DE EMPLEADOS                                                   ");
        System.out.println("==============================================================================================================");
        System.out.println("Texto buscado: " + busqueda);
        System.out.println("--------------------------------------------------------------------------------------------------------------");

        if (empleados == null || empleados.isEmpty()) {
            System.out.println("No se encontraron empleados con ese nombre o apellido.");
        } else {
            System.out.printf("%-15s %-20s %-20s %-30s %-35s%n",
                    "Nombres", "Apellido Paterno", "Apellido Materno", "Correo", "Password Hash");
            System.out.println("--------------------------------------------------------------------------------------------------------------");

            for (Empleado e : empleados) {
                String password = e.getPassword();

                if (password != null && password.length() > 35) {
                    password = password.substring(0, 35) + "...";
                }

                System.out.printf("%-15s %-20s %-20s %-30s %-35s%n",
                        e.getNombres(),
                        e.getApellidoPaterno(),
                        e.getApellidoMaterno() != null ? e.getApellidoMaterno() : "",
                        e.getCorreo(),
                        password != null ? password : "");
            }
        }

        System.out.println("==============================================================================================================");
    }
    public static void probarLoginEmpleado(String correo, String password) throws Exception {
        int resultado = empleadoBO.verificarCuenta(correo, password);

        System.out.println("==============================================");
        System.out.println("              VALIDACIÓN EMPLEADO             ");
        System.out.println("==============================================");
        System.out.println("Correo ingresado: " + correo);

        if (resultado == 1) {
            System.out.println("Resultado: Cuenta válida.");
            System.out.println("Mensaje: El correo y la contraseña son correctos.");
        } else {
            System.out.println("Resultado: Cuenta inválida.");
            System.out.println("Mensaje: El correo o la contraseña son incorrectos.");
        }

        System.out.println("==============================================");
    }
}
