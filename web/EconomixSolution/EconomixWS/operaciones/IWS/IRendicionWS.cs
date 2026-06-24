namespace EconomixWS.OperacionesWS;

using EconomixModel.Model;
using EconomixWS.UsuarioWS;

public interface IRendicionWS : IWS<Rendicion>
{
    Rendicion? obtenerPorId(int id);
    Rendicion? generarRendicionDeCiclo(int idCiclo, int idUsuarioAccion);
    List<Rendicion> listarPorArea(int idArea);
    void observarRendicion(int idRendicion, string comentario, int idUsuarioAccion);
    void aceptarRendicion(int idRendicion, int idUsuarioAccion);
    void denegarRendicion(int idRendicion, string comentario, int idUsuarioAccion);
    void reEnviarRendicion(int idRendicion, int idUsuarioAccion);
}
