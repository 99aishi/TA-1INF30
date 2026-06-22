using System.Text.Json.Serialization;

namespace EconomixModel.Model;

public class Rendicion
{
    [JsonPropertyName("idRendicion")]
    public int IdRendicion { get; set; }

    [JsonPropertyName("fechaPresentacion")]
    public DateTime? FechaPresentacion { get; set; }

    [JsonPropertyName("fechaAprobacion")]
    public DateTime? FechaAprobacion { get; set; }

    [JsonPropertyName("totalDeclarado")]
    public double TotalDeclarado { get; set; }

    [JsonPropertyName("totalAprobado")]
    public double TotalAprobado { get; set; }

    [JsonPropertyName("saldoFinal")]
    public double SaldoFinal { get; set; }

    [JsonPropertyName("estado")]
    public string Estado { get; set; } = "EN_ESPERA";

    [JsonPropertyName("comentario")]
    public string? Comentario { get; set; }

    [JsonPropertyName("cicloCajaChica")]
    public CicloCajaChica? CicloCajaChica { get; set; }
}
