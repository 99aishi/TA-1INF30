package pe.edu.pucp.economix.rrhh.test;

import pe.edu.pucp.economix.rrhh.boi.RolBOImpl;
import pe.edu.pucp.economix.rrhh.ibo.IRolBO;
import pe.edu.pucp.economix.rrhh.model.Rol;

import java.util.List;

public class RolTest {
    public static List<Rol> pruebaInsercion() throws Exception {
        IRolBO rolBO = new RolBOImpl();
        Rol rolGerenteGeneral = new Rol("Gerente General", "Responsable de la empresa");
        rolGerenteGeneral.setRolID(rolBO.insertar(rolGerenteGeneral, 1));
        Rol rolGerente = new Rol("Gerente de Área", "Responsable de departamento");
        rolGerente.setRolID(rolBO.insertar(rolGerente, 1));
        Rol rolAnalista = new Rol("Analista Senior", "Especialista con experiencia");
        rolAnalista.setRolID(rolBO.insertar(rolAnalista, 1));

        List<Rol> listaRoles = rolBO.listarTodas();
        for (Rol r : listaRoles) {
            System.out.println(r);
        }
        System.out.println();
        return listaRoles;
    }
}
