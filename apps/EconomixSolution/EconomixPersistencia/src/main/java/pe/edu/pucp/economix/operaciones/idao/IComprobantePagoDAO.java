package pe.edu.pucp.economix.operaciones.idao;

import pe.edu.pucp.economix.idao.IDAO;
import pe.edu.pucp.economix.operaciones.model.ComprobantePago;

import java.sql.SQLException;
import java.util.List;

public interface IComprobantePagoDAO extends IDAO<ComprobantePago> {
    public List<ComprobantePago> listarPorSolicitud(int idSolicitud) throws SQLException;
}
