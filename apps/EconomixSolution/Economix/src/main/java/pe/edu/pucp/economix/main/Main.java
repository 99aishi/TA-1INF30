package pe.edu.pucp.economix.main;

import pe.edu.pucp.economix.rrhh.boi.AdministradorBOImpl;
import pe.edu.pucp.economix.rrhh.boi.AreaBOImpl;
import pe.edu.pucp.economix.rrhh.ibo.IAdministradorBO;
import pe.edu.pucp.economix.rrhh.ibo.IAreaBO;
import pe.edu.pucp.economix.rrhh.model.Administrador;
import pe.edu.pucp.economix.rrhh.model.Area;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        IAdministradorBO administradorBO = new AdministradorBOImpl();
        Administrador admin = administradorBO.buscarPorId(1);
        if (admin == null) {
            admin = new Administrador();
            admin.setNombres("Administrador");
            admin.setCorreo("admin@economix.pe");
            admin.setApellidoPaterno("Principal");
            admin.setPassword("administrador123");
            administradorBO.insertar(admin, 1);
            System.out.println("Administrador insertado con ID: " + admin.getUsuarioID());
        } else {
            System.out.println("Administrador ya existe: " + admin.getNombres());
        }
    }
}
