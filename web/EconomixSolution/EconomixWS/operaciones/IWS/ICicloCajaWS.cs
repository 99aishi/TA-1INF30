using System.Threading.Tasks;
using EconomixModel.Model;
using EconomixWS.UsuarioWS;

namespace EconomixWS.OperacionesWS;

public interface ICicloCajaWS : IWS<CicloCajaChica>
{
    Task<CicloCajaChica?> obtenerPorIdAsync(int id);
    Task cerrarCicloAsync(int id, int idUsuarioAccion);
}
