using System.Collections.Generic;
using System.Threading.Tasks;
using EconomixModel.Model;
using EconomixWS.UsuarioWS;

namespace EconomixWS.OperacionesWS;

public interface IRendicionWS : IWS<Rendicion>
{
    Task<Rendicion?> obtenerPorIdAsync(int id);
    Task<Rendicion?> generarRendicionDeCicloAsync(int idCiclo, int idUsuarioAccion);
    Task<List<Rendicion>> listarPorAreaAsync(int idArea);
    Task observarRendicionAsync(int idRendicion, string comentario, int idUsuarioAccion);
    Task aceptarRendicionAsync(int idRendicion, int idUsuarioAccion);
    Task denegarRendicionAsync(int idRendicion, string comentario, int idUsuarioAccion);
    Task reEnviarRendicionAsync(int idRendicion, int idUsuarioAccion);
}
