package pe.edu.pucp.economix.operaciones.boi;

import pe.edu.pucp.economix.operaciones.daoi.PermisoEdicionDAOImpl;
import pe.edu.pucp.economix.operaciones.idao.IPermisoEdicionDAO;
import pe.edu.pucp.economix.operaciones.ibo.IPermisoEdicionBO;
import pe.edu.pucp.economix.operaciones.model.ComprobantePago;
import pe.edu.pucp.economix.operaciones.model.PermisoEdicion;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoCicloCaja;

import java.util.List;

public class PermisoEdicionBOImpl implements IPermisoEdicionBO {
    private final IPermisoEdicionDAO permisoEdicionDAO;

    public PermisoEdicionBOImpl() {
        this.permisoEdicionDAO = new PermisoEdicionDAOImpl();
    }

    private void validarIdUsuarioAccion(int idUsuarioAccion) throws Exception {
        if (idUsuarioAccion <= 0) {
            throw new Exception("El usuario de accion debe ser mayor que cero.");
        }
    }

    @Override
    public int solicitar(PermisoEdicion permiso, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(permiso, false);
        return permisoEdicionDAO.solicitar(permiso, idUsuarioAccion);
    }

    @Override
    public int otorgar(int idPermiso, int idAutorizador, String motivoAutorizacion, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        if (idPermiso <= 0) {
            throw new Exception("El ID del permiso debe ser mayor que cero.");
        }
        if (idAutorizador <= 0) {
            throw new Exception("El ID del autorizador debe ser mayor que cero.");
        }
        return permisoEdicionDAO.otorgar(idPermiso, idAutorizador, motivoAutorizacion, idUsuarioAccion);
    }

    @Override
    public int revocar(int idPermiso, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        if (idPermiso <= 0) {
            throw new Exception("El ID del permiso debe ser mayor que cero.");
        }
        return permisoEdicionDAO.revocar(idPermiso, idUsuarioAccion);
    }

    @Override
    public List<PermisoEdicion> listarPendientes(int idAutorizador) throws Exception {
        if (idAutorizador <= 0) {
            throw new Exception("El ID del autorizador debe ser mayor que cero.");
        }
        return permisoEdicionDAO.listarPendientes(idAutorizador);
    }

    @Override
    public List<PermisoEdicion> listarComprobantesEnExcepcion() throws Exception {
        return permisoEdicionDAO.listarComprobantesEnExcepcion();
    }

    private void validar(PermisoEdicion permiso, boolean esModificacion) throws Exception {
        if (permiso == null) {
            throw new Exception("El permiso no puede ser nulo.");
        }
        if (permiso.getComprobante() == null || permiso.getComprobante().getIdComprobante() <= 0) {
            throw new Exception("El comprobante asociado es invalido.");
        }
        if (permiso.getSolicitante() == null || permiso.getSolicitante().getUsuarioID() <= 0) {
            throw new Exception("El solicitante es invalido.");
        }

        ComprobantePago cp = permiso.getComprobante();
        SolicitudGasto sg = cp.getSolicitud();
        if (sg == null || sg.getCiclo() == null) {
            throw new Exception("El comprobante debe estar asociado a una solicitud con ciclo.");
        }

        EstadoCicloCaja estadoCiclo = sg.getCiclo().getEstado();
        if (estadoCiclo != EstadoCicloCaja.CERRADO
                && estadoCiclo != EstadoCicloCaja.LIQUIDADO
                && estadoCiclo != EstadoCicloCaja.EN_EXCEPCION) {
            throw new Exception("No se requiere permiso de edicion para este ciclo.");
        }
    }
}
