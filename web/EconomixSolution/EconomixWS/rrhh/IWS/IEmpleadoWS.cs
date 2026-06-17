namespace EconomixWS.UsuarioWS;

using EconomixModel.Model;

public interface IEmpleadoWS : IWS<Empleado>
{
    Empleado? obtenerPorId(int id);
    List<Empleado> listarPorNombreApellido(string q);
}
