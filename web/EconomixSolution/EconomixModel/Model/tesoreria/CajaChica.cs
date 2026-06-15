using System.Text.Json.Serialization;

namespace EconomixModel.Model;

public class CajaChica
{
    [JsonPropertyName("idFondo")]
    public int IdFondo { get; set; }

    [JsonPropertyName("nombre")]
    public string Nombre { get; set; } = string.Empty;

    [JsonPropertyName("montoTecho")]
    public double MontoTecho { get; set; }

    [JsonPropertyName("estado")]
    public string Estado { get; set; } = "Activo";

    [JsonPropertyName("areaAsignada")]
    public Area? AreaAsignada { get; set; }
}
