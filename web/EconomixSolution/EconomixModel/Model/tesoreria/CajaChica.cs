using System.Text.Json.Serialization;

namespace EconomixModel.Model;

public class CajaChica
{
    [JsonPropertyName("idFondo")]
    public int IdFondo { get; set; }

    [JsonPropertyName("nombre")]
    public string Nombre { get; set; } = string.Empty;

    [JsonPropertyName("saldoActual")]
    public double SaldoActual { get; set; }

    [JsonPropertyName("montoTecho")]
    public double MontoTecho { get; set; }

    [JsonPropertyName("estado")]
    public string Estado { get; set; } = "ACTIVO";

    [JsonPropertyName("moneda")]
    public Moneda? Moneda { get; set; }

    [JsonPropertyName("cuentaOrigen")]
    public CuentaBancaria? CuentaOrigen { get; set; }

    [JsonPropertyName("areaAsignada")]
    public Area? AreaAsignada { get; set; }
}
