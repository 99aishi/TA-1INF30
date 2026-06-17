package pe.edu.pucp.economix.operaciones.ibo;

import pe.edu.pucp.economix.ibo.IBaseBO;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;

import java.util.List;

public interface ICicloCajaBO extends IBaseBO<CicloCajaChica> {

    void calcularTotalGastado(CicloCajaChica cicloCajaChica, int idUsuarioAccion) throws Exception;
    List<CicloCajaChica> listarActivos() throws Exception;
}
