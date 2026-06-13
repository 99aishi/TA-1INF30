using System.Text.Json.Serialization;

namespace EconomixModel.Model;

public class LoginResponse
{
    [JsonPropertyName("id_usuario")]
    public int IdUsuario { get; set; }

    [JsonPropertyName("nombres")]
    public string Nombres { get; set; } = string.Empty;

    [JsonPropertyName("apellido_paterno")]
    public string ApellidoPaterno { get; set; } = string.Empty;

    [JsonPropertyName("apellido_materno")]
    public string ApellidoMaterno { get; set; } = string.Empty;

    [JsonPropertyName("password_hash")]
    public string PasswordHash { get; set; } = string.Empty;

    [JsonPropertyName("estado")]
    public string Estado { get; set; } = string.Empty;

    [JsonPropertyName("tipo_usuario")]
    public string TipoUsuario { get; set; } = string.Empty;

    [JsonPropertyName("id_empleado")]
    public int? IdEmpleado { get; set; }

    [JsonPropertyName("id_area")]
    public int? IdArea { get; set; }

    [JsonPropertyName("nombre_area")]
    public string? NombreArea { get; set; }

    [JsonPropertyName("id_rol")]
    public int? IdRol { get; set; }

    [JsonPropertyName("titulo_rol")]
    public string? TituloRol { get; set; }
}
