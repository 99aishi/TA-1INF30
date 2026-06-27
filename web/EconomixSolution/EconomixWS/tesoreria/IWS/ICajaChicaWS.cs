using System.Collections.Generic;
using System.Threading.Tasks;
using EconomixModel.Model;
using EconomixWS.UsuarioWS;

namespace EconomixWS.TesoreriaWS;

public interface ICajaChicaWS : IWS<CajaChica>
{
    Task<CajaChica?> obtenerPorIdAsync(int id);
    Task<List<CajaChica>> listarPorCuentaBancariaAsync(int idCuentaBancaria);
}
