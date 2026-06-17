namespace EconomixModel.Model;

using System.Text.Json.Serialization;

public class Area
{
    [JsonPropertyName("idArea")]
    public int AreaID { get; set; }

    [JsonPropertyName("nombre")]
    public string Nombre { get; set; } = string.Empty;
    [JsonPropertyName("descripcion")]
    public string Descripcion { get; set; } = string.Empty;

    [JsonPropertyName("jefe")]
    public Empleado? Jefe { get; set; }

    [JsonPropertyName("estaActivo")]
    public bool EstaActivo { get; set; }

    [JsonPropertyName("cajaChica")]
    public CajaChica? CajaChica { get; set; }

    [JsonPropertyName("cuentasBancarias")]
    public List<CuentaBancaria> CuentasBancarias { get; set; } = new();
}
