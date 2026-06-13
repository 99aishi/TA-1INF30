using System.Text.Json.Serialization;

namespace EconomixModel.Model;

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
}
