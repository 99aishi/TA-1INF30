using System.Collections.Generic;
using System.Threading.Tasks;
using EconomixModel.Model;

namespace EconomixWS.TesoreriaWS;

public interface ITipoCambioWS
{
    Task<TipoCambio?> buscarPorMonedasYFechaAsync(int idMonedaOrigen, int idMonedaDestino, DateTime? fecha = null);
    Task<TipoCambio?> obtenerPorIdAsync(int id);
    Task<List<TipoCambio>> listarAsync();
}
