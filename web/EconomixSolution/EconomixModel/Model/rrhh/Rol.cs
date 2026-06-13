using System.Text.Json.Serialization;

namespace EconomixModel.Model;

public class Rol
{
    [JsonPropertyName("rolID")]
    public int RolID { get; set; }

    [JsonPropertyName("titulo")]
    public string Titulo { get; set; } = string.Empty;

    [JsonPropertyName("descripcion")]
    public string Descripcion { get; set; } = string.Empty;
}
