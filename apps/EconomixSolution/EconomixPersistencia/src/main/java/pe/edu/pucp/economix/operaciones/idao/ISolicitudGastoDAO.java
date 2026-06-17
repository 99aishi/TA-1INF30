package pe.edu.pucp.economix.operaciones.idao;

import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;

import java.sql.SQLException;
import java.util.List;

public interface ISolicitudGastoDAO {
    int insertar(SolicitudGasto solicitudGasto, int idUsuarioAccion) throws SQLException;
    int modificar(SolicitudGasto solicitudGasto, int idUsuarioAccion) throws SQLException;
    int eliminar(int id, int idUsuarioAccion) throws SQLException;
    SolicitudGasto buscarPorId(int id) throws SQLException;
    List<SolicitudGasto> listarActivas() throws SQLException;
    List<SolicitudGasto> listarTodas() throws SQLException;
    List<SolicitudGasto> listarPorSolicitante(int idUsuarioSolicitante) throws SQLException;
    List<SolicitudGasto> listarPendientesJefe(int idUsuarioDestinatario) throws SQLException;
    List<SolicitudGasto> listarPorCiclo(int idCicloCaja) throws SQLException;
}
