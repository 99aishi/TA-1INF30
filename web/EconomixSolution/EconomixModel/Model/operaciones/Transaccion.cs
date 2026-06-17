using System.Text.Json.Serialization;

namespace EconomixModel.Model;

public class Transaccion
{
    [JsonPropertyName("idTransaccion")]
    public int IdTransaccion { get; set; }

    [JsonPropertyName("tipoTransaccion")]
    public string TipoTransaccion { get; set; } = "DESEMBOLSO";

    [JsonPropertyName("fecha")]
    public DateTime Fecha { get; set; }

    [JsonPropertyName("monto")]
    public double Monto { get; set; }

    [JsonPropertyName("numeroOperacionBancaria")]
    public string? NumeroOperacionBancaria { get; set; }

    [JsonPropertyName("medioPago")]
    public string MedioPago { get; set; } = "TRANSFERENCIA";

    [JsonPropertyName("tipoCambio")]
    public TipoCambio? TipoCambio { get; set; }

    [JsonPropertyName("estadoTransaccion")]
    public string EstadoTransaccion { get; set; } = "REGISTRADA";

    [JsonPropertyName("cuentaOrigen")]
    public CuentaBancaria? CuentaOrigen { get; set; }

    [JsonPropertyName("cuentaDestino")]
    public CuentaBancaria? CuentaDestino { get; set; }

    [JsonPropertyName("moneda")]
    public Moneda? Moneda { get; set; }

    [JsonPropertyName("beneficiario")]
    public Empleado? Beneficiario { get; set; }
}
