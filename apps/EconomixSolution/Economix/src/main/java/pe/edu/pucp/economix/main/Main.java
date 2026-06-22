package pe.edu.pucp.economix.main;

import pe.edu.pucp.economix.rrhh.boi.*;
import pe.edu.pucp.economix.rrhh.ibo.*;
import pe.edu.pucp.economix.rrhh.model.Administrador;
import pe.edu.pucp.economix.rrhh.model.Empleado;

public class Main {
    public static void main(String[] args) throws Exception {
        IAdministradorBO administradorBO = new AdministradorBOImpl();
        Administrador admin = administradorBO.buscarPorId(11);
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

        IUsuarioBO empleadoBO = new UsuarioBOImpl();
        String correo = "admin@economix.pe";
        String password = "administrador123";
        empleadoBO.validarUsuario(correo, password);
    }
}
