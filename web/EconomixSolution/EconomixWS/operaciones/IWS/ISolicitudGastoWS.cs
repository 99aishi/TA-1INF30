using System.Collections.Generic;
using System.Threading.Tasks;
using EconomixModel.Model;
using EconomixWS.UsuarioWS;

namespace EconomixWS.OperacionesWS;

public interface ISolicitudGastoWS : IWS<SolicitudGasto>
{
    Task<SolicitudGasto?> obtenerPorIdAsync(int id);
    Task<List<SolicitudGasto>> listarPorSolicitanteAsync(int idSolicitante);
    Task<List<SolicitudGasto>> listarPendientesJefeAsync(int idJefe);
    Task<List<SolicitudGasto>> listarPorCicloAsync(int idCiclo);
    Task<SolicitudGasto?> evaluarAsync(int idSolicitudGasto, string accion, string comentario, int idJefeEvaluador, int idUsuarioAccion);
}
