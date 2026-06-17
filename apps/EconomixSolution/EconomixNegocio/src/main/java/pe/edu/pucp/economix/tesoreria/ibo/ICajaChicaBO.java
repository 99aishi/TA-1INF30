package pe.edu.pucp.economix.tesoreria.ibo;

import pe.edu.pucp.economix.ibo.IBaseBO;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;

import java.util.List;

public interface ICajaChicaBO extends IBaseBO<CajaChica> {
    List<CajaChica> listarActivas() throws Exception;
}
