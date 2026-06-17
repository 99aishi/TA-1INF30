namespace EconomixWS.UsuarioWS;

using EconomixModel.Model;

public interface IAreaWS : IWS<Area>
{
    Area? obtenerPorId(int id);
    List<Area> listarActivas();
    int recuperar(int id, int idUsuarioAccion);
}