using EconomixModel.Model;

namespace EconomixWS.UsuarioWS;

public interface IAuditoriaWS
{
    List<AuditLogEntry> listarRecientes(int limite);
}
