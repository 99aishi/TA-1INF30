package pe.edu.pucp.economix.operaciones.ibo;

import pe.edu.pucp.economix.ibo.IBaseBO;
import pe.edu.pucp.economix.operaciones.model.Rendicion;

import java.util.List;

public interface IRendicionBO extends IBaseBO<Rendicion> {
    public void calcularTotales(Rendicion rendicion, int idUsuarioAccion) throws Exception;
    public Rendicion generarRendicionDeCiclo(int idCiclo, int idUsuarioAccion) throws Exception;
    public List<Rendicion> listarActivas() throws Exception;
    public void observarRendicion(int idRendicion, String comentario, int idUsuarioAccion) throws Exception;
    public void aceptarRendicion(int idRendicion, int idUsuarioAccion) throws Exception;
    public void denegarRendicion(int idRendicion, String comentario, int idUsuarioAccion) throws Exception;
    public void reEnviarRendicion(int idRendicion, int idUsuarioAccion) throws Exception;
    public List<Rendicion> listarPorArea(int idArea) throws Exception;
}
