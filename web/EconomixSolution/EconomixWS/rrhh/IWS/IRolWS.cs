using System.Collections.Generic;
using System.Threading.Tasks;
using EconomixModel.Model;

namespace EconomixWS.UsuarioWS;

public interface IRolWS : IWS<Rol>
{
    Task<Rol?> obtenerPorIdAsync(int id);
    Task<List<Rol>> listarPorAreaAsync(int idArea);
}
