using System.Text.Json.Serialization;

namespace EconomixModel.Model;

public class ComprobantePago
{
    [JsonPropertyName("idComprobante")]
    public int IdComprobante { get; set; }

    [JsonPropertyName("tipoDocumento")]
    public string TipoDocumento { get; set; } = "FACTURA";

    [JsonPropertyName("RUCProveedor")]
    public string? RucProveedor { get; set; }

    [JsonPropertyName("razonSocial")]
    public string? RazonSocial { get; set; }

    [JsonPropertyName("numeroSerial")]
    public string? NumeroSerial { get; set; }

    [JsonPropertyName("fechaEmision")]
    public DateTime? FechaEmision { get; set; }

    [JsonPropertyName("montoTotal")]
    public double MontoTotal { get; set; }

    [JsonPropertyName("tipoCambio")]
    public double TipoCambio { get; set; } = 1.0;

    [JsonPropertyName("montoConvertido")]
    public double MontoConvertido { get; set; }

    [JsonPropertyName("nombreArchivoComprobante")]
    public string NombreArchivoComprobante { get; set; } = string.Empty;

    [JsonPropertyName("subtotal")]
    public double Subtotal { get; set; }

    [JsonPropertyName("igv")]
    public double Igv { get; set; }

    [JsonPropertyName("total")]
    public double Total { get; set; }

    [JsonPropertyName("estado")]
    public string Estado { get; set; } = "POR_REVISAR";

    [JsonPropertyName("solicitud")]
    public SolicitudGasto? Solicitud { get; set; }

    [JsonPropertyName("moneda")]
    public Moneda? Moneda { get; set; }
}
