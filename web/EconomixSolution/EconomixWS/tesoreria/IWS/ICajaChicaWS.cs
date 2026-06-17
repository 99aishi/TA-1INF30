namespace EconomixWS.TesoreriaWS;

using EconomixModel.Model;
using EconomixWS.UsuarioWS;

public interface ICajaChicaWS : IWS<CajaChica>
{
    CajaChica? obtenerPorId(int id);
}
