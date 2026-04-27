package pe.edu.pucp.economix.operaciones.test;

import pe.edu.pucp.economix.operaciones.implement.SolicitudGastoImplement;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.EstadoSolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.rrhh.model.Empleado;

import java.util.Date;
import java.util.List;

public class SolicitudGastoTest {
    private static SolicitudGastoImplement solicitudDAO = new SolicitudGastoImplement();
    public static List<SolicitudGasto> pruebaInsercion(CicloCajaChica cicloActivo, Empleado solicitante, Empleado aprobador){

        SolicitudGasto sol1 = new SolicitudGasto();
        sol1.setFechaSolicitud(new Date());
        sol1.setMontoSolicitado(150.00);
        sol1.setMotivoSolicitud("Compra de teclado mecánico para desarrollador");

        sol1.setEstado(EstadoSolicitudGasto.valueOf("Aprobado"));
        sol1.setSolicitante(solicitante);
        sol1.setDestinatario(aprobador);
        sol1.setCiclo(cicloActivo);
        sol1.setIdSolicitudGasto(solicitudDAO.insertar(sol1));


        SolicitudGasto sol2 = new SolicitudGasto();
        sol2.setFechaSolicitud(new Date());
        sol2.setMontoSolicitado(45.50);
        sol2.setMotivoSolicitud("Movilidad a reunión con cliente");
        sol2.setEstado(EstadoSolicitudGasto.valueOf("Pendiente"));
        sol2.setSolicitante(solicitante);
        sol2.setDestinatario(aprobador);
        sol2.setCiclo(cicloActivo);
        sol2.setIdSolicitudGasto(solicitudDAO.insertar(sol2));

        List<SolicitudGasto> solicitudes = solicitudDAO.listarTodas();
        for (SolicitudGasto s : solicitudes) {
            System.out.println("SOLICITUD: " + s.getIdSolicitudGasto() + " | Motivo: " + s.getMotivoSolicitud() + " | Monto: " + s.getMontoSolicitado());
        }
        System.out.println();
        return solicitudes;
    }

}
