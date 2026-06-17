namespace EconomixWS.OperacionesWS;

using EconomixModel.Model;
using EconomixWS.UsuarioWS;

public interface IComprobantePagoWS : IWS<ComprobantePago>
{
    ComprobantePago? obtenerPorId(int id);
    List<ComprobantePago> listarPorSolicitud(int idSolicitud);
    void evaluar(int idComprobante, bool aprobar, string observacion, int idUsuarioAccion);
}
