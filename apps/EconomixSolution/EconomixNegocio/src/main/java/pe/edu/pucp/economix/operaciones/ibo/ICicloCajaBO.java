package pe.edu.pucp.economix.operaciones.ibo;

import pe.edu.pucp.economix.ibo.IBaseBO;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;

public interface ICicloCajaBO extends IBaseBO<CicloCajaChica> {

    void calcularTotalGastado(CicloCajaChica cicloCajaChica) throws Exception;
}
