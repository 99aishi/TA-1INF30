namespace EconomixWS.TesoreriaWS;

using EconomixModel.Model;

public interface ITipoCambioWS
{
    TipoCambio? buscarPorMonedasYFecha(int idMonedaOrigen, int idMonedaDestino, DateTime? fecha = null);
    TipoCambio? obtenerPorId(int id);
    List<TipoCambio> listar();
}
