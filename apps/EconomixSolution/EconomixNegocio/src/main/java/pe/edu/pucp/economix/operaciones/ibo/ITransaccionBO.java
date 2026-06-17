package pe.edu.pucp.economix.operaciones.ibo;

import pe.edu.pucp.economix.ibo.IBaseBO;
import pe.edu.pucp.economix.operaciones.model.Transaccion;

import java.util.List;

public interface ITransaccionBO extends IBaseBO<Transaccion> {
    List<Transaccion> listarActivas() throws Exception;
}
