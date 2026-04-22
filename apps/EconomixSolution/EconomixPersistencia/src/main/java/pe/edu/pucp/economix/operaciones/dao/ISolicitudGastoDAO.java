package pe.edu.pucp.economix.operaciones.dao;

import pe.edu.pucp.economix.dao.IDAO;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;

import java.util.List;

public interface ISolicitudGastoDAO extends IDAO<SolicitudGasto> {
    List<SolicitudGasto> listarPorSolicitante(int idUsuarioSolicitante);
    List<SolicitudGasto> listarPendientesJefe(int idUsuarioDestinatario);
}
