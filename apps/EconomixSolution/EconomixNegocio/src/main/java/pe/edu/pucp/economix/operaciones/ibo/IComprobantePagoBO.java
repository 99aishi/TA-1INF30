package pe.edu.pucp.economix.operaciones.ibo;

import pe.edu.pucp.economix.ibo.IBaseBO;
import pe.edu.pucp.economix.operaciones.model.ComprobantePago;

import java.util.List;

public interface IComprobantePagoBO extends IBaseBO<ComprobantePago> {
    List<ComprobantePago> listarPorSolicitud(int idSolicitud) throws Exception;
    List<ComprobantePago> listarActivas() throws Exception;
}
