using System.Text.Json.Serialization;

namespace EconomixModel.Model;

public class SolicitudGasto
{
    [JsonPropertyName("idSolicitudGasto")]
    public int IdSolicitudGasto { get; set; }

    [JsonPropertyName("fechaSolicitud")]
    public DateTime FechaSolicitud { get; set; }

    [JsonPropertyName("montoSolicitado")]
    public double MontoSolicitado { get; set; }

    [JsonPropertyName("monedaOriginal")]
    public Moneda? MonedaOriginal { get; set; }

    [JsonPropertyName("tipoCambio")]
    public double TipoCambio { get; set; } = 1.0;

    [JsonPropertyName("montoConvertido")]
    public double MontoConvertido { get; set; }

    [JsonPropertyName("motivoSolicitud")]
    public string MotivoSolicitud { get; set; } = string.Empty;

    [JsonPropertyName("estado")]
    public string Estado { get; set; } = "PENDIENTE";

    [JsonPropertyName("medioDesembolso")]
    public string MedioDesembolso { get; set; } = string.Empty;

    [JsonPropertyName("solicitante")]
    public Empleado? Solicitante { get; set; }

    [JsonPropertyName("destinatario")]
    public Empleado? Destinatario { get; set; }

    [JsonPropertyName("jefeAprobador")]
    public Empleado? JefeAprobador { get; set; }

    [JsonPropertyName("tesoreroAprobador")]
    public Empleado? TesoreroAprobador { get; set; }

    [JsonPropertyName("ciclo")]
    public CicloCajaChica? Ciclo { get; set; }

    [JsonPropertyName("comprobantes")]
    public List<ComprobantePago>? Comprobantes { get; set; }
}
