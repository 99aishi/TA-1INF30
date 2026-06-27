using System.Collections.Generic;
using System.Threading.Tasks;
using EconomixModel.Model;
using EconomixWS.UsuarioWS;

namespace EconomixWS.TesoreriaWS;

public interface ICuentaBancariaWS : IWS<CuentaBancaria>
{
    Task<CuentaBancaria?> obtenerPorIdAsync(int id);
    Task<List<CuentaBancaria>> listarPorEmpleadoAsync(int idEmpleado);
    Task<List<CajaChica>> listarCajasChicasAsync(int idCuentaBancaria);
}
