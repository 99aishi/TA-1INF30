package pe.edu.pucp.economix.operaciones.idao;

import java.sql.SQLException;
import java.util.List;

import pe.edu.pucp.economix.operaciones.model.PermisoEdicion;

public interface IPermisoEdicionDAO {
    int solicitar(PermisoEdicion permiso, int idUsuarioAccion) throws SQLException;
    int otorgar(int idPermiso, int idAutorizador, String motivoAutorizacion, int idUsuarioAccion) throws SQLException;
    int revocar(int idPermiso, int idUsuarioAccion) throws SQLException;
    List<PermisoEdicion> listarPendientes(int idAutorizador) throws SQLException;
    List<PermisoEdicion> listarComprobantesEnExcepcion() throws SQLException;
}
