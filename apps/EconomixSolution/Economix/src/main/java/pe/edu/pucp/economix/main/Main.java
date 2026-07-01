package pe.edu.pucp.economix.main;

import pe.edu.pucp.economix.operaciones.boi.CicloCajaBOImpl;
import pe.edu.pucp.economix.operaciones.boi.RendicionBOImpl;
import pe.edu.pucp.economix.operaciones.boi.SolicitudGastoBOImpl;
import pe.edu.pucp.economix.operaciones.boi.TransaccionBOImpl;
import pe.edu.pucp.economix.operaciones.ibo.ICicloCajaBO;
import pe.edu.pucp.economix.operaciones.ibo.IRendicionBO;
import pe.edu.pucp.economix.operaciones.ibo.ISolicitudGastoBO;
import pe.edu.pucp.economix.operaciones.ibo.ITransaccionBO;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.Transaccion;
import pe.edu.pucp.economix.rrhh.boi.*;
import pe.edu.pucp.economix.rrhh.ibo.*;
import pe.edu.pucp.economix.rrhh.model.Administrador;
import pe.edu.pucp.economix.rrhh.model.Empleado;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        IAdministradorBO administradorBO = new AdministradorBOImpl();
//        Administrador admin = administradorBO.buscarPorId(11);
//        if (admin == null) {
//            admin = new Administrador();
//            admin.setNombres("Administrador");
//            admin.setCorreo("admin@economix.pe");
//            admin.setApellidoPaterno("Principal");
//            admin.setPassword("administrador123");
//            administradorBO.insertar(admin, 1);
//            System.out.println("Administrador insertado con ID: " + admin.getUsuarioID());
//        } else {
//            System.out.println("Administrador ya existe: " + admin.getNombres());
//        }

        ITransaccionBO transaccionBO = new TransaccionBOImpl();
        List<Transaccion> transacciones = transaccionBO.listarPorJefe(2);
        IRendicionBO rendicionBO = new RendicionBOImpl();
        rendicionBO.listarTodas();
    }
}
