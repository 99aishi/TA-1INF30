using System.Collections.Generic;
using System.Threading.Tasks;
using EconomixModel.Model;

namespace EconomixWS.UsuarioWS;

public interface IEmpleadoWS : IWS<Empleado>
{
    Task<Empleado?> obtenerPorIdAsync(int id);
    Task<List<Empleado>> listarPorNombreApellidoAsync(string q);
    Task<List<Empleado>> listarPorAreaAsync(int idArea);
}
