using System.Collections.Generic;
using System.Threading.Tasks;
using EconomixModel.Model;

namespace EconomixWS.UsuarioWS;

public interface IAreaWS : IWS<Area>
{
    Task<Area?> obtenerPorIdAsync(int id);
    Task<List<Area>> listarActivasAsync();
    Task<int> recuperarAsync(int id, int idUsuarioAccion);
}