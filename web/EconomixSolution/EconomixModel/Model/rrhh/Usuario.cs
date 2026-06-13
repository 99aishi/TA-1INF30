using System.Text.Json.Serialization;

namespace EconomixModel.Model;

[JsonPolymorphic(
    TypeDiscriminatorPropertyName = "$type", 
    UnknownDerivedTypeHandling = JsonUnknownDerivedTypeHandling.FallBackToNearestAncestor // 👈 ESTO
)]
[JsonDerivedType(typeof(Empleado), typeDiscriminator: "empleado")]
[JsonDerivedType(typeof(Administrador), typeDiscriminator: "administrador")]
public abstract class Usuario
{
    [JsonPropertyName("usuarioID")]
    public int UsuarioID { get; set; }

    [JsonPropertyName("nombres")]
    public string Nombres { get; set; } = string.Empty;

    [JsonPropertyName("apellidoPaterno")]
    public string ApellidoPaterno { get; set; } = string.Empty;

    [JsonPropertyName("apellidoMaterno")]
    public string ApellidoMaterno { get; set; } = string.Empty;

    [JsonPropertyName("password")]
    public string Password { get; set; } = string.Empty;

    [JsonPropertyName("estado")]
    public string Estado { get; set; } = string.Empty;
}
