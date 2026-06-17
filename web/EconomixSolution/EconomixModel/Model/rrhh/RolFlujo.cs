using System.Text.Json.Serialization;

namespace EconomixModel.Model;

[JsonConverter(typeof(JsonStringEnumConverter))]
public enum RolFlujo
{
    EMPLEADO,
    JEFE_AREA
}
