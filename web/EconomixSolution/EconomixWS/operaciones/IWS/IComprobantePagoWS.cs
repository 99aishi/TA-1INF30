using System.Collections.Generic;
using System.Threading.Tasks;
using EconomixModel.Model;
using EconomixWS.UsuarioWS;

namespace EconomixWS.OperacionesWS;

public interface IComprobantePagoWS : IWS<ComprobantePago>
{
    Task<ComprobantePago?> obtenerPorIdAsync(int id);
    Task<List<ComprobantePago>> listarPorSolicitudAsync(int idSolicitud);
    Task evaluarAsync(int idComprobante, bool aprobar, string observacion, int idUsuarioAccion);
}
