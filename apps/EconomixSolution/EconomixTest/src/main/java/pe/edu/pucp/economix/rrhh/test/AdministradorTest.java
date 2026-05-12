package pe.edu.pucp.economix.rrhh.test;

import pe.edu.pucp.economix.rrhh.implement.AdministradorImplement;
import pe.edu.pucp.economix.rrhh.model.Administrador;
import pe.edu.pucp.economix.rrhh.model.EstadoUsuario;

import java.sql.SQLException;
import java.util.List;

public class AdministradorTest {
    public static List<Administrador> pruebaInsercion() throws SQLException {
        Administrador admin1 = new Administrador();
        AdministradorImplement adminDAO = new AdministradorImplement();
        admin1.setNombres("Soporte");
        admin1.setApellidoPaterno("Técnico");
        admin1.setCorreoSoporte("soporte@empresa.com");
        admin1.setPassword("TILIN");
        admin1.setEstado(EstadoUsuario.Activo);
        admin1.setUsuarioID(adminDAO.insertar(admin1));

        List<Administrador> listaAdmins = adminDAO.listarTodas();
        for (Administrador ad : listaAdmins) {
            System.out.println(ad.toString());
        }
        System.out.println();
        return listaAdmins;
    }
}
