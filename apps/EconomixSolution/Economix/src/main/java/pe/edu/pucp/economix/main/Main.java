package pe.edu.pucp.economix.main;

import java.sql.SQLException;
import java.util.List;

import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.ComprobantePago;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.operaciones.test.CicloCajaChicaTest;
import pe.edu.pucp.economix.operaciones.test.ComprobantePagoTest;
import pe.edu.pucp.economix.operaciones.test.SolicitudGastoTest;
import pe.edu.pucp.economix.rrhh.boi.*;
import pe.edu.pucp.economix.rrhh.ibo.*;
import pe.edu.pucp.economix.rrhh.model.*;
import pe.edu.pucp.economix.rrhh.test.AdministradorTest;
import pe.edu.pucp.economix.rrhh.test.AreaTest;
import pe.edu.pucp.economix.rrhh.test.EmpleadoTest;
import pe.edu.pucp.economix.rrhh.test.RolTest;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;
import pe.edu.pucp.economix.tesoreria.model.Moneda;
import pe.edu.pucp.economix.tesoreria.test.CajaChicaTest;
import pe.edu.pucp.economix.tesoreria.test.CuentaBancariaTest;
import pe.edu.pucp.economix.tesoreria.test.MonedaTest;

public class Main {
    public static void main(String []args) throws Exception {
        Administrador admin = new Administrador();
        admin.setNombres("Administrador");
        admin.setCorreo("admin@economix.pe");
        admin.setApellidoPaterno("Principal");
        admin.setPassword("administrador123");

        IAdministradorBO administradorBO = new AdministradorBOImpl();
        administradorBO.insertar(admin);

        IAreaBO areaBO = new AreaBOImpl();
        List<Area> areas = areaBO.listarTodas();



    }

}
