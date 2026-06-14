using System.Text.Json.Serialization;

namespace EconomixModel.Model;

public class Empleado : Usuario
{
    [JsonPropertyName("numeroCelular")]
    public string NumeroCelular { get; set; } = string.Empty;

    [JsonPropertyName("rol")]
    public override Rol? Rol { get; set; }

    [JsonPropertyName("area")]
    public Area? Area { get; set; }

    [JsonPropertyName("jefeDirecto")]
    public Empleado? JefeDirecto { get; set;}
}
