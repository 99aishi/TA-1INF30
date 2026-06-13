using System.Text.Json.Serialization;

namespace EconomixModel.Model;

public class Empleado : Usuario
{
    [JsonPropertyName("correoInstitucional")]
    public string CorreoInstitucional { get; set; } = string.Empty;

    [JsonPropertyName("numeroCelular")]
    public string NumeroCelular { get; set; } = string.Empty;

    [JsonPropertyName("rol")]
    public Rol? Rol { get; set; }

    [JsonPropertyName("area")]
    public Area? Area { get; set; }

    [JsonPropertyName("jefeDirecto")]
    public Empleado? JefeDirecto { get; set; }
}
