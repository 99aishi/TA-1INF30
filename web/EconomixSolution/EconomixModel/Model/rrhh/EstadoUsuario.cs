using System.Text.Json.Serialization;

namespace EconomixModel.Model;

[JsonConverter(typeof(JsonStringEnumConverter))]
public enum EstadoUsuario
{
    ACTIVO,
    INACTIVO
}
