package pe.edu.pucp.economix.operaciones.ibo;

import pe.edu.pucp.economix.ibo.IBaseBO;
import pe.edu.pucp.economix.operaciones.model.Rendicion;

public interface IRendicionBO extends IBaseBO<Rendicion> {
    public void calcularTotales(Rendicion rendicion) throws Exception;
    public Rendicion generarRendicionDeCiclo(int idCiclo) throws Exception;
}
