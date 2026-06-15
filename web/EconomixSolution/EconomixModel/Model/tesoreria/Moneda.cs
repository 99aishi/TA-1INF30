using System.Text.Json.Serialization;

namespace EconomixModel.Model;

public class Moneda
{
    [JsonPropertyName("idMoneda")]
    public int IdMoneda { get; set; }

    [JsonPropertyName("codigoISO")]
    public string CodigoISO { get; set; } = string.Empty;

    [JsonPropertyName("simbolo")]
    public string Simbolo { get; set; } = string.Empty;

    [JsonPropertyName("nombre")]
    public string Nombre { get; set; } = string.Empty;

    [JsonPropertyName("descripcion")]
    public string Descripcion { get; set; } = string.Empty;

    [JsonPropertyName("activa")]
    public bool Activa { get; set; } = true;
}
