package pe.edu.pucp.economix.operaciones.ibo;

import java.util.List;

import pe.edu.pucp.economix.operaciones.model.PermisoEdicion;

public interface IPermisoEdicionBO {
    int solicitar(PermisoEdicion permiso, int idUsuarioAccion) throws Exception;
    int otorgar(int idPermiso, int idAutorizador, String motivoAutorizacion, int idUsuarioAccion) throws Exception;
    int revocar(int idPermiso, int idUsuarioAccion) throws Exception;
    List<PermisoEdicion> listarPendientes(int idAutorizador) throws Exception;
    List<PermisoEdicion> listarComprobantesEnExcepcion() throws Exception;
}
