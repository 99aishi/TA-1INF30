package pe.edu.pucp.economix.operaciones.idao;

import java.sql.SQLException;
import java.util.List;

import pe.edu.pucp.economix.idao.IDAO;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;

public interface ISolicitudGastoDAO extends IDAO<SolicitudGasto> {
    List<SolicitudGasto> listarPorSolicitante(int idUsuarioSolicitante) throws SQLException;
    List<SolicitudGasto> listarPendientesJefe(int idUsuarioDestinatario) throws SQLException ;
    List<SolicitudGasto> listarPorCiclo(int idCicloCaja) throws SQLException;
}
