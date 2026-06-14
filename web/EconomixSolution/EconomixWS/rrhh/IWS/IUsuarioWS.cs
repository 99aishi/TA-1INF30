using EconomixModel.Model;

namespace EconomixWS.UsuarioWS;

public interface IUsuarioWS
{
    Usuario? ValidarCredenciales(LoginRequest request);
    bool IsAuthenticated();
    Usuario? GetCurrentUser();
    void Logout();
}
