package pe.edu.pucp.economix.rrhh.test;

import pe.edu.pucp.economix.rrhh.implement.AdministradorImplement;
import pe.edu.pucp.economix.rrhh.model.Administrador;
import pe.edu.pucp.economix.rrhh.model.EstadoUsuario;

import java.util.List;

public class AdministradorTest {
    public static List<Administrador> pruebaInsercion(){
        Administrador admin1 = new Administrador();
        AdministradorImplement adminDAO = new AdministradorImplement();
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
        System.out.println();
        return listaAdmins;
    }
}
