using System.Collections.Generic;
using System.Threading.Tasks;
using EconomixModel.Model;

namespace EconomixWS.OperacionesWS;

public interface IPermisoEdicionWS
{
    Task SolicitarAsync(PermisoEdicion permiso, int idUsuarioAccion);
    Task OtorgarAsync(int idPermiso, int idAutorizador, string motivoAutorizacion, int idUsuarioAccion);
    Task RevocarAsync(int idPermiso, int idUsuarioAccion);
    Task<List<PermisoEdicion>> ListarPendientesAsync(int idAutorizador);
    Task<List<PermisoEdicion>> ListarEnExcepcionAsync();
}
