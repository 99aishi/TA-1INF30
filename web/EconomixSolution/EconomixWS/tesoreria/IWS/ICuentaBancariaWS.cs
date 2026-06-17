namespace EconomixWS.TesoreriaWS;

using EconomixModel.Model;
using EconomixWS.UsuarioWS;

public interface ICuentaBancariaWS : IWS<CuentaBancaria>
{
    CuentaBancaria? obtenerPorId(int id);
}
