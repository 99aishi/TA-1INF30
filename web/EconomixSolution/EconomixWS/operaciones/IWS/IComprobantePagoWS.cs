namespace EconomixWS.OperacionesWS;

using EconomixModel.Model;
using EconomixWS.UsuarioWS;

public interface IComprobantePagoWS : IWS<ComprobantePago>
{
    ComprobantePago? obtenerPorId(int id);
    List<ComprobantePago> listarPorSolicitud(int idSolicitud);
}
