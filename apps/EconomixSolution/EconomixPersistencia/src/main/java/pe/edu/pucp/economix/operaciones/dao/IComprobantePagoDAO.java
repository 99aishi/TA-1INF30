package pe.edu.pucp.economix.operaciones.dao;

import pe.edu.pucp.economix.dao.IDAO;
import pe.edu.pucp.economix.operaciones.model.ComprobantePago;

import java.util.List;

public interface IComprobantePagoDAO extends IDAO<ComprobantePago> {
    List<ComprobantePago> listarPorSolicitud(int idSolicitud);
}
