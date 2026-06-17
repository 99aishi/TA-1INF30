package pe.edu.pucp.economix.rrhh.test;

import pe.edu.pucp.economix.rrhh.boi.AdministradorBOImpl;
import pe.edu.pucp.economix.rrhh.boi.EmpleadoBOImpl;
import pe.edu.pucp.economix.rrhh.ibo.IAdministradorBO;
import pe.edu.pucp.economix.rrhh.ibo.IEmpleadoBO;
import pe.edu.pucp.economix.rrhh.model.Administrador;
import pe.edu.pucp.economix.rrhh.model.EstadoUsuario;

import java.util.List;

public class AdministradorTest {
    private static IAdministradorBO administradorBO = new AdministradorBOImpl();
    public static List<Administrador> pruebaInsercion() throws Exception {
        Administrador admin1 = new Administrador();
        IAdministradorBO adminBO = new AdministradorBOImpl();
        admin1.setNombres("Soporte");
        admin1.setApellidoPaterno("Técnico");
        admin1.setApellidoMaterno("De TI");
        admin1.setCorreo("soporte@empresa.com");
        admin1.setPassword("TILIN");
        admin1.setEstado(EstadoUsuario.ACTIVO);

        admin1.setUsuarioID(adminBO.insertar(admin1, 1));

        List<Administrador> listaAdmins = adminBO.listarTodas();
        for (Administrador ad : listaAdmins) {
            System.out.println(ad.toString());
        }
        System.out.println();
        return listaAdmins;
    }
    public static void probarLoginAdministrador(String correo, String password) throws Exception {
        int resultado = administradorBO.verificarCuenta(correo, password);

        System.out.println("==============================================");
        System.out.println("           VALIDACIÓN ADMINISTRADOR           ");
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
