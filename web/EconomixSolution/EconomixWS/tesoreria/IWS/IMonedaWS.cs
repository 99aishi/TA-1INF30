using System.Collections.Generic;
using System.Threading.Tasks;
using EconomixModel.Model;
using EconomixWS.UsuarioWS;

namespace EconomixWS.TesoreriaWS;

public interface IMonedaWS : IWS<Moneda>
{
    Task<Moneda?> obtenerPorIdAsync(int id);
    Task<List<Moneda>> buscarMonedasAsync(string q);
    Task<List<Moneda>> listarPorEstadoAsync(bool activa);
    Task<List<Moneda>> listarActivasAsync();
    Task<int> recuperarAsync(int id, int idUsuarioAccion);
}
