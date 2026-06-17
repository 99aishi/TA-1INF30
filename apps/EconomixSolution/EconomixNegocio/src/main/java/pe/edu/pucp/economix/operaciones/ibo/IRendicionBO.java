package pe.edu.pucp.economix.operaciones.ibo;

import pe.edu.pucp.economix.ibo.IBaseBO;
import pe.edu.pucp.economix.operaciones.model.Rendicion;

import java.util.List;

public interface IRendicionBO extends IBaseBO<Rendicion> {
    public void calcularTotales(Rendicion rendicion, int idUsuarioAccion) throws Exception;
    public Rendicion generarRendicionDeCiclo(int idCiclo, int idUsuarioAccion) throws Exception;
    public List<Rendicion> listarActivas() throws Exception;
}
