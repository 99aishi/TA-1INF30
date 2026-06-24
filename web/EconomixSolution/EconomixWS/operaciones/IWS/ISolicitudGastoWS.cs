namespace EconomixWS.OperacionesWS;

using EconomixModel.Model;
using EconomixWS.UsuarioWS;

public interface ISolicitudGastoWS : IWS<SolicitudGasto>
{
    SolicitudGasto? obtenerPorId(int id);
    List<SolicitudGasto> listarPorSolicitante(int idSolicitante);
    List<SolicitudGasto> listarPendientesJefe(int idJefe);
    List<SolicitudGasto> listarPorCiclo(int idCiclo);
    SolicitudGasto? evaluar(int idSolicitudGasto, bool aprobado, string comentario, int idJefeEvaluador, int idUsuarioAccion);
    void ejecutarDesembolso(int idSolicitudGasto, string medioDesembolso, int idCuentaDestino, string numeroOperacionBancaria, int idUsuarioAccion);
}
