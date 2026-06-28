namespace EconomixWS.UsuarioWS;

using System.Collections.Generic;
using System.Threading.Tasks;
using EconomixModel.Model;


public interface IAuditoriaWS
{
    List<AuditLogEntry> listarRecientes(int limite);
    Task<List<AuditLogEntry>> listarRecientesAsync(int limite);
}
