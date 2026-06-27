using System.Security.Claims;
using System.Text.Json;
using System.Text.Json.Serialization;
using EconomixModel.Model;
using Microsoft.AspNetCore.Http;

namespace EconomixWS.OperacionesWS;

public class ComprobantePagoWSImpl : IComprobantePagoWS
{
    private readonly HttpClient _httpClient;
    private readonly IHttpContextAccessor _httpContextAccessor;
    private readonly JsonSerializerOptions _jsonOptions;

    public ComprobantePagoWSImpl(HttpClient httpClient, IHttpContextAccessor httpContextAccessor, JsonSerializerOptions jsonOptions)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(httpClient.BaseAddress + "ComprobantePagoWS/");
        _httpContextAccessor = httpContextAccessor;
        _jsonOptions = jsonOptions;
    }

    private int ObtenerIdUsuarioAccion()
    {
        var user = _httpContextAccessor.HttpContext?.User;
        if (user?.Identity?.IsAuthenticated != true)
            throw new UnauthorizedAccessException("No hay una sesión activa.");

        var nameClaim = user.FindFirst(ClaimTypes.NameIdentifier)
            ?? throw new UnauthorizedAccessException("No se encontró el identificador de usuario en la sesión.");

        return int.Parse(nameClaim.Value);
    }

    public async Task insertarAsync(ComprobantePago obj, int idUsuarioAccion)
    {
        var response = await _httpClient.PostAsJsonAsync($"Insertar?idUsuarioAccion={idUsuarioAccion}", obj, _jsonOptions);
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
    }

    public async Task actualizarAsync(ComprobantePago obj, int idUsuarioAccion)
    {
        var response = await _httpClient.PostAsJsonAsync($"Actualizar?idUsuarioAccion={idUsuarioAccion}", obj, _jsonOptions);
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
    }

    public async Task eliminarAsync(int id, int idUsuarioAccion)
    {
        var response = await _httpClient.GetAsync($"Eliminar?id={id}&idUsuarioAccion={idUsuarioAccion}");
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
    }

    public async Task<List<ComprobantePago>> listarAsync()
    {
        try
        {
            var response = await _httpClient.GetAsync("Listar");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<ComprobantePago>();
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<ComprobantePago>();
            return JsonSerializer.Deserialize<List<ComprobantePago>>(json, _jsonOptions) ?? new List<ComprobantePago>();
        }
        catch
        {
            return new List<ComprobantePago>();
        }
    }

    public async Task<ComprobantePago?> obtenerPorIdAsync(int id)
    {
        try
        {
            var response = await _httpClient.GetAsync($"BuscarPorId?id={id}");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return null;
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return null;
            return JsonSerializer.Deserialize<ComprobantePago>(json, _jsonOptions);
        }
        catch
        {
            return null;
        }
    }
    public async Task<List<ComprobantePago>> listarPorSolicitudAsync(int idSolicitud)
    {
        try
        {
            var response = await _httpClient.GetAsync($"ListarPorSolicitud?idSolicitud={idSolicitud}");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<ComprobantePago>();
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<ComprobantePago>();
            return JsonSerializer.Deserialize<List<ComprobantePago>>(json, _jsonOptions) ?? new List<ComprobantePago>();
        }
        catch
        {
            return new List<ComprobantePago>();
        }
    }

    public async Task evaluarAsync(int idComprobante, bool aprobar, string observacion, int idUsuarioAccion)
    {
        var url = $"Evaluar?idComprobante={idComprobante}&aprobar={aprobar.ToString().ToLowerInvariant()}&observacion={Uri.EscapeDataString(observacion ?? "")}&idUsuarioAccion={idUsuarioAccion}";
        var response = await _httpClient.PostAsync(url, null);
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
    }

}
