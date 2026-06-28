using System.Threading.Tasks;
using EconomixModel.Model;
using EconomixWS.UsuarioWS;

namespace EconomixWS.OperacionesWS;

public interface ITransaccionWS : IWS<Transaccion>
{
    Task<Transaccion?> obtenerPorIdAsync(int id);
    Task<List<Transaccion>> listarPorJefeAsync(int idJefe);
    Task<List<Transaccion>> listarPorEmpleadoAsync(int idEmpleado);
}
