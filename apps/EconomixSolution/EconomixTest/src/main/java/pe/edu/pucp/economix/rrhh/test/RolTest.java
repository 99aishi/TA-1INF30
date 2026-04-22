package pe.edu.pucp.economix.rrhh.test;

import pe.edu.pucp.economix.rrhh.implement.RolImplement;
import pe.edu.pucp.economix.rrhh.model.Rol;

import java.util.List;

public class RolTest {
    public static List<Rol> pruebaInsercion(){
        RolImplement rolDAO = new RolImplement();
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
        return listaRoles;
    }
}
