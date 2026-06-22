using System.Text.Json.Serialization;

namespace EconomixModel.Model;

public class CuentaBancaria
{
    [JsonPropertyName("idCuenta")]
    public int IdCuenta { get; set; }

    [JsonPropertyName("nombreBanco")]
    public string NombreBanco { get; set; } = string.Empty;

    [JsonPropertyName("numeroBancario")]
    public string NumeroBancario { get; set; } = string.Empty;

    [JsonPropertyName("cci")]
    public string? Cci { get; set; }

    [JsonPropertyName("moneda")]
    public Moneda? Moneda { get; set; }

    [JsonPropertyName("empleadoAdministrador")]
    public Empleado? EmpleadoAdministrador { get; set; }

    [JsonPropertyName("areaAdministradora")]
    public Area? AreaAdministradora { get; set; }

    [JsonPropertyName("cajasChicas")]
    public List<CajaChica> CajasChicas { get; set; } = new();

    [JsonPropertyName("activa")]
    public bool Activa { get; set; } = true;
}
