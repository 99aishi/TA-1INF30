using System.Text.Json.Serialization;

namespace EconomixModel.Model;

public abstract class Usuario
{
    [JsonPropertyName("usuarioID")]
    public int UsuarioID { get; set; }

    [JsonPropertyName("nombres")]
    public string Nombres { get; set; } = string.Empty;

    [JsonPropertyName("apellidoPaterno")]
    public string ApellidoPaterno { get; set; } = string.Empty;

    [JsonPropertyName("apellidoMaterno")]
    public string ApellidoMaterno { get; set; } = string.Empty;

    [JsonPropertyName("password")]
    public string Password { get; set; } = string.Empty;

    [JsonPropertyName("estado")]
    public string Estado { get; set; } = string.Empty;

    [JsonPropertyName("correo")]
    public string Correo { get; set; } = string.Empty;

    [JsonPropertyName("rol")]
    public virtual Rol? Rol { get; set; }
}
