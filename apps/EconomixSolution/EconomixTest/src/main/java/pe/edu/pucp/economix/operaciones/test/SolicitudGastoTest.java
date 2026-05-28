package pe.edu.pucp.economix.operaciones.test;

import java.util.Date;
import java.util.List;

import pe.edu.pucp.economix.operaciones.boi.SolicitudGastoBOImpl;
import pe.edu.pucp.economix.operaciones.ibo.ISolicitudGastoBO;
import pe.edu.pucp.economix.operaciones.daoi.SolicitudGastoDAOImpl;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoSolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.rrhh.model.Empleado;

public class SolicitudGastoTest {
    private static final SolicitudGastoDAOImpl solicitudDAO = new SolicitudGastoDAOImpl();
    private static final ISolicitudGastoBO solicitudGastoBO = new SolicitudGastoBOImpl();

    public static List<SolicitudGasto> pruebaInsercion(CicloCajaChica cicloActivo, Empleado solicitante, Empleado aprobador) throws Exception {

        SolicitudGasto sol1 = new SolicitudGasto();
        sol1.setFechaSolicitud(new Date());
        sol1.setMontoSolicitado(150.00);
        sol1.setMotivoSolicitud("Compra de teclado mecánico para desarrollador");

        sol1.setEstado(EstadoSolicitudGasto.Aprobado);
        sol1.setSolicitante(solicitante);
        sol1.setDestinatario(aprobador);
        sol1.setCiclo(cicloActivo);
        sol1.setIdSolicitudGasto(solicitudGastoBO.insertar(sol1));


        SolicitudGasto sol2 = new SolicitudGasto();
        sol2.setFechaSolicitud(new Date());
        sol2.setMontoSolicitado(45.50);
        sol2.setMotivoSolicitud("Movilidad a reunión con cliente");
        sol2.setEstado(EstadoSolicitudGasto.Pendiente);
        sol2.setSolicitante(solicitante);
        sol2.setDestinatario(aprobador);
        sol2.setCiclo(cicloActivo);
        sol2.setIdSolicitudGasto(solicitudGastoBO.insertar(sol2));

        List<SolicitudGasto> solicitudes = solicitudGastoBO.listarTodas();
        for (SolicitudGasto s : solicitudes) {
            System.out.println(s);
        }
        System.out.println();
        return solicitudes;
    }

}
