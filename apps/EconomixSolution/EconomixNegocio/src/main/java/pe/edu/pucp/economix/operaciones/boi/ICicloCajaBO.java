package pe.edu.pucp.economix.operaciones.boi;

import pe.edu.pucp.economix.bo.IBaseBO;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;

public interface ICicloCajaBO extends IBaseBO<CicloCajaChica> {

    void calcularTotalGastado(CicloCajaChica cicloCajaChica) throws Exception;
}
