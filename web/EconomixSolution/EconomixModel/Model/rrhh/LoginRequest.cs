using System.Text.Json.Serialization;

namespace EconomixModel.Model;

public class LoginRequest
{
    [JsonPropertyName("correo")]
    public string Correo { get; set; } = string.Empty;

    [JsonPropertyName("password")]
    public string Password { get; set; } = string.Empty;
}
