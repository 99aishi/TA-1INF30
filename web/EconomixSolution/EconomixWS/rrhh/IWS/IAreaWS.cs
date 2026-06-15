namespace EconomixWS.UsuarioWS;

using EconomixModel.Model;

public interface IAreaWS : IWS<Area>
{
    Area? obtenerPorId(int id);
}