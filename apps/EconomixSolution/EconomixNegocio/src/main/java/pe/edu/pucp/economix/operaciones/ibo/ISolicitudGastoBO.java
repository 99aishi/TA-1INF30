package pe.edu.pucp.economix.operaciones.ibo;

import pe.edu.pucp.economix.ibo.IBaseBO;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;

import java.util.List;

public interface ISolicitudGastoBO extends IBaseBO<SolicitudGasto> {
    public List<SolicitudGasto> listarPorSolicitante(int idSolicitante) throws Exception;
    public List<SolicitudGasto> listarPendientesJefe(int idUsuarioDestinatario) throws Exception;
    public List<SolicitudGasto> listarPorCiclo(int idCicloCaja) throws Exception;
    public List<SolicitudGasto> listarActivas() throws Exception;
    public int evaluar(int idSolicitudGasto, boolean aprobado, String comentario, int idJefeEvaluador, String numeroOperacionBancaria, int idUsuarioAccion) throws Exception;
}
