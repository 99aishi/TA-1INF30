package pe.edu.pucp.economix.operaciones.idao;

import pe.edu.pucp.economix.operaciones.model.ComprobantePago;

import java.sql.SQLException;
import java.util.List;

public interface IComprobantePagoDAO {
    int insertar(ComprobantePago comprobante, int idUsuarioAccion) throws SQLException;
    int modificar(ComprobantePago comprobante, int idUsuarioAccion) throws SQLException;
    int eliminar(int id, int idUsuarioAccion) throws SQLException;
    ComprobantePago buscarPorId(int id) throws SQLException;
    List<ComprobantePago> listarActivas() throws SQLException;
    List<ComprobantePago> listarTodas() throws SQLException;
    List<ComprobantePago> listarPorSolicitud(int idSolicitud) throws SQLException;
}
