package pe.edu.pucp.economix.rrhh.test;

import pe.edu.pucp.economix.rrhh.bo.AdministradorBOImpl;
import pe.edu.pucp.economix.rrhh.boi.IAdministradorBO;
import pe.edu.pucp.economix.rrhh.implement.AdministradorImplement;
import pe.edu.pucp.economix.rrhh.model.Administrador;
import pe.edu.pucp.economix.rrhh.model.EstadoUsuario;

import java.sql.SQLException;
import java.util.List;

public class AdministradorTest {
    public static List<Administrador> pruebaInsercion() throws Exception {
        Administrador admin1 = new Administrador();
        IAdministradorBO adminBO = new AdministradorBOImpl();
        admin1.setNombres("Soporte");
        admin1.setApellidoPaterno("Técnico");
        admin1.setApellidoMaterno("De TI");
        admin1.setCorreoSoporte("soporte@empresa.com");
        admin1.setPassword("TILIN");
        admin1.setEstado(EstadoUsuario.Activo);

        admin1.setUsuarioID(adminBO.insertar(admin1));

        List<Administrador> listaAdmins = adminBO.listarTodas();
        for (Administrador ad : listaAdmins) {
            System.out.println(ad.toString());
        }
        System.out.println();
        return listaAdmins;
    }
}
