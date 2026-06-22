using System.Text.Json.Serialization;

namespace EconomixModel.Model;

public class AuditLogEntry
{
    [JsonPropertyName("idAuditoria")]
    public int IdAuditoria { get; set; }

    [JsonPropertyName("nombreTabla")]
    public string NombreTabla { get; set; } = string.Empty;

    [JsonPropertyName("tipoEvento")]
    public string TipoEvento { get; set; } = string.Empty;

    [JsonPropertyName("idRegistroAfectado")]
    public string IdRegistroAfectado { get; set; } = string.Empty;

    [JsonPropertyName("descripcion")]
    public string Descripcion { get; set; } = string.Empty;

    [JsonPropertyName("entidadNombre")]
    public string EntidadNombre { get; set; } = string.Empty;

    [JsonPropertyName("accionNombre")]
    public string AccionNombre { get; set; } = string.Empty;

    [JsonPropertyName("creadoAt")]
    public DateTime CreadoAt { get; set; }

    [JsonPropertyName("idUsuarioAuditoria")]
    public int IdUsuarioAuditoria { get; set; }

    [JsonPropertyName("usuarioNombres")]
    public string? UsuarioNombres { get; set; }

    [JsonPropertyName("usuarioApellidoPaterno")]
    public string? UsuarioApellidoPaterno { get; set; }

    [JsonPropertyName("nombreCompletoUsuario")]
    public string NombreCompletoUsuario { get; set; } = string.Empty;
}
