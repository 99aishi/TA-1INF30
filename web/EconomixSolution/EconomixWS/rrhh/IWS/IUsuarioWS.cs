using System.Threading.Tasks;
using System.Threading.Tasks;
using EconomixModel.Model;

namespace EconomixWS.UsuarioWS;

public interface IUsuarioWS
{
    Task<Usuario?> ValidarCredencialesAsync(LoginRequest request);
    bool IsAuthenticated();
    Usuario? GetCurrentUser();
    int GetCurrentUserId();
    void Logout();
}
