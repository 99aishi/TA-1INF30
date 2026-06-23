using EconomixModel.Model;

namespace EconomixWS.OperacionesWS;

public interface IPermisoEdicionWS
{
    void Solicitar(PermisoEdicion permiso, int idUsuarioAccion);
    void Otorgar(int idPermiso, int idAutorizador, string motivoAutorizacion, int idUsuarioAccion);
    void Revocar(int idPermiso, int idUsuarioAccion);
    List<PermisoEdicion> ListarPendientes(int idAutorizador);
    List<PermisoEdicion> ListarEnExcepcion();
}
