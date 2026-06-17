namespace EconomixWS.TesoreriaWS;

using EconomixModel.Model;
using EconomixWS.UsuarioWS;

public interface IMonedaWS : IWS<Moneda>
{
    Moneda? obtenerPorId(int id);
    List<Moneda> buscarMonedas(string q);
    List<Moneda> listarPorEstado(bool activa);
    List<Moneda> listarActivas();
    int recuperar(int id, int idUsuarioAccion);
}
