namespace EconomixWS.OperacionesWS;

using EconomixModel.Model;
using EconomixWS.UsuarioWS;

public interface ITransaccionWS : IWS<Transaccion>
{
    Transaccion? obtenerPorId(int id);
}
