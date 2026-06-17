using System.Text.Json.Serialization;

namespace EconomixModel.Model;

public class TipoCambio
{
    [JsonPropertyName("idTipoCambio")]
    public int IdTipoCambio { get; set; }

    [JsonPropertyName("monedaOrigen")]
    public Moneda? MonedaOrigen { get; set; }

    [JsonPropertyName("monedaDestino")]
    public Moneda? MonedaDestino { get; set; }

    [JsonPropertyName("valor")]
    public double Valor { get; set; } = 1.0;

    [JsonPropertyName("fecha")]
    public DateTime Fecha { get; set; }
}
