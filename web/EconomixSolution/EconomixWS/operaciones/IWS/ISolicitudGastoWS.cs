namespace EconomixWS.OperacionesWS;

using EconomixModel.Model;
using EconomixWS.UsuarioWS;

public interface ISolicitudGastoWS : IWS<SolicitudGasto>
{
    SolicitudGasto? obtenerPorId(int id);
    List<SolicitudGasto> listarPorSolicitante(int idSolicitante);
    List<SolicitudGasto> listarPendientesJefe(int idJefe);
    List<SolicitudGasto> listarPorCiclo(int idCiclo);
    void evaluar(int idSolicitudGasto, bool aprobado, string comentario, int idJefeEvaluador, string numeroOperacionBancaria, int idUsuarioAccion);
    void evaluar(int idSolicitudGasto, bool aprobado, string comentario, int idJefeEvaluador, int idUsuarioAccion, string medioDesembolso, int idCuentaDestino);
    void ejecutarDesembolso(int idSolicitudGasto, string numeroOperacionBancaria, int idUsuarioAccion);
}
