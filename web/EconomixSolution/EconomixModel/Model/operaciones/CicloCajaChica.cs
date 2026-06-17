using System.Text.Json.Serialization;

namespace EconomixModel.Model;

public class CicloCajaChica
{
    [JsonPropertyName("idCicloCaja")]
    public int IdCicloCaja { get; set; }

    [JsonPropertyName("numeroSemana")]
    public int NumeroSemana { get; set; }

    [JsonPropertyName("fechaApertura")]
    public DateTime? FechaApertura { get; set; }

    [JsonPropertyName("fechaCierre")]
    public DateTime? FechaCierre { get; set; }

    [JsonPropertyName("saldoInicial")]
    public double SaldoInicial { get; set; }

    [JsonPropertyName("totalGastado")]
    public double TotalGastado { get; set; }

    [JsonPropertyName("estado")]
    public string Estado { get; set; } = "ABIERTO";

    [JsonPropertyName("cajaChica")]
    public CajaChica? CajaChica { get; set; }

    [JsonPropertyName("solicitudesDeGasto")]
    public List<SolicitudGasto>? SolicitudesDeGasto { get; set; }
}
