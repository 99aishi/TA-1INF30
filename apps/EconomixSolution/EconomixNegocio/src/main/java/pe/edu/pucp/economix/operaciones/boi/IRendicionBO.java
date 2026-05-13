package pe.edu.pucp.economix.operaciones.boi;

import pe.edu.pucp.economix.bo.IBaseBO;
import pe.edu.pucp.economix.operaciones.model.Rendicion;

public interface IRendicionBO extends IBaseBO<Rendicion> {
    public void calcularTotales(Rendicion rendicion) throws Exception;
}
