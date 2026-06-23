using System.Text.Json.Serialization;

namespace EconomixModel.Model;

public class PermisoEdicion
{
    [JsonPropertyName("idPermiso")]
    public int IdPermiso { get; set; }

    [JsonPropertyName("fechaCreacion")]
    public DateTime? FechaCreacion { get; set; }

    [JsonPropertyName("fechaExpiracion")]
    public DateTime? FechaExpiracion { get; set; }

    [JsonPropertyName("fechaUso")]
    public DateTime? FechaUso { get; set; }

    [JsonPropertyName("motivoSolicitud")]
    public string? MotivoSolicitud { get; set; }

    [JsonPropertyName("motivoAutorizacion")]
    public string? MotivoAutorizacion { get; set; }

    [JsonPropertyName("estado")]
    public string Estado { get; set; } = "ACTIVO";

    [JsonPropertyName("solicitante")]
    public Empleado? Solicitante { get; set; }

    [JsonPropertyName("autorizador")]
    public Empleado? Autorizador { get; set; }

    [JsonPropertyName("comprobante")]
    public ComprobantePago? Comprobante { get; set; }
}
