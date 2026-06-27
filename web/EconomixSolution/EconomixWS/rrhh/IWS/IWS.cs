using System.Collections.Generic;
using System.Threading.Tasks;

namespace EconomixWS.UsuarioWS;

public interface IWS<T>
{
    Task insertarAsync(T objeto, int idUsuarioAccion);
    Task actualizarAsync(T objeto, int idUsuarioAccion);
    Task eliminarAsync(int id, int idUsuarioAccion);
    Task<List<T>> listarAsync();
}