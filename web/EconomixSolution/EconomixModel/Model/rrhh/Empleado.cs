using System.Text.Json.Serialization;

namespace EconomixModel.Model;

public class Empleado : Usuario
{
    [JsonPropertyName("numeroCelular")]
    public string NumeroCelular { get; set; } = string.Empty;

    [JsonPropertyName("rolFlujo")]
    public RolFlujo RolFlujo { get; set; } = RolFlujo.EMPLEADO;

    [JsonPropertyName("rol")]
    public override Rol? Rol { get; set; }

    [JsonPropertyName("area")]
    public Area? Area { get; set; }

    [JsonPropertyName("jefeDirecto")]
    public Empleado? JefeDirecto { get; set;}

    [JsonPropertyName("cuentas")]
    public List<CuentaBancaria> Cuentas { get; set; } = new();

    [JsonPropertyName("solicitudesRecibidas")]
    public List<SolicitudGasto> SolicitudesRecibidas { get; set; } = new();

    [JsonPropertyName("solicitudesEnviadas")]
    public List<SolicitudGasto> SolicitudesEnviadas { get; set; } = new();
}
