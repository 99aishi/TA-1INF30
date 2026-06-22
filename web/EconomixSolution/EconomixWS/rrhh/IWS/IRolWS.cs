namespace EconomixWS.UsuarioWS;

using EconomixModel.Model;

public interface IRolWS : IWS<Rol>
{
    Rol? obtenerPorId(int id);
    List<Rol> listarPorArea(int idArea);
}
