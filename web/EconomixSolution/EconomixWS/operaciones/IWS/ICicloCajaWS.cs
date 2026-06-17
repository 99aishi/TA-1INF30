namespace EconomixWS.OperacionesWS;

using EconomixModel.Model;
using EconomixWS.UsuarioWS;

public interface ICicloCajaWS : IWS<CicloCajaChica>
{
    CicloCajaChica? obtenerPorId(int id);
}
