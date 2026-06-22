using System.Text.Json;
using EconomixModel.Model;

namespace EconomixWS.UsuarioWS;

public class AuditoriaWSImpl : IAuditoriaWS
{
    private readonly HttpClient _httpClient;
    private readonly JsonSerializerOptions _jsonOptions;

    public AuditoriaWSImpl(HttpClient httpClient, JsonSerializerOptions jsonOptions)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(httpClient.BaseAddress + "AuditoriaWS/");
        _jsonOptions = jsonOptions;
    }

    public List<AuditLogEntry> listarRecientes(int limite)
    {
        var response = _httpClient.GetAsync($"ListarRecientes?limite={limite}").GetAwaiter().GetResult();

        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }

        if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
            return new List<AuditLogEntry>();

        var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
        if (string.IsNullOrEmpty(json) || json == "null")
            return new List<AuditLogEntry>();

        return System.Text.Json.JsonSerializer.Deserialize<List<AuditLogEntry>>(json, _jsonOptions) ?? new List<AuditLogEntry>();
    }
}
