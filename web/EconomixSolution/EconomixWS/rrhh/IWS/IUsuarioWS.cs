using EconomixModel.Model;

namespace EconomixWS.UsuarioWS;

public interface IUsuarioWS
{
    Task<Usuario?> ValidarCredencialesAsync(LoginRequest request);
}
