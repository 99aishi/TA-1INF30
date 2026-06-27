using System.Security.Claims;
using System.Text.Json;
using System.Text.Json.Serialization;
using EconomixModel.Model;
using Microsoft.AspNetCore.Http;

namespace EconomixWS.OperacionesWS;

public class SolicitudGastoWSImpl : ISolicitudGastoWS
{
    private readonly HttpClient _httpClient;
    private readonly IHttpContextAccessor _httpContextAccessor;
    private readonly JsonSerializerOptions _jsonOptions;

    public SolicitudGastoWSImpl(HttpClient httpClient, IHttpContextAccessor httpContextAccessor, JsonSerializerOptions jsonOptions)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(httpClient.BaseAddress + "SolicitudGastoWS/");
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
    public async Task insertarAsync(SolicitudGasto obj, int idUsuarioAccion)
    {
        var response = await _httpClient.PostAsJsonAsync($"Insertar?idUsuarioAccion={idUsuarioAccion}", obj, _jsonOptions);
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
    }

    public async Task actualizarAsync(SolicitudGasto obj, int idUsuarioAccion)
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

    public async Task<List<SolicitudGasto>> listarAsync()
    {
        try
        {
            var response = await _httpClient.GetAsync("Listar");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<SolicitudGasto>();
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<SolicitudGasto>();
            return JsonSerializer.Deserialize<List<SolicitudGasto>>(json, _jsonOptions) ?? new List<SolicitudGasto>();
        }
        catch
        {
            return new List<SolicitudGasto>();
        }
    }

    public async Task<SolicitudGasto?> obtenerPorIdAsync(int id)
    {
        try
        {
            var response = await _httpClient.GetAsync($"BuscarPorId?id={id}");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return null;
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return null;
            return JsonSerializer.Deserialize<SolicitudGasto>(json, _jsonOptions);
        }
        catch
        {
            return null;
        }
    }

    public async Task<List<SolicitudGasto>> listarPorSolicitanteAsync(int idSolicitante)
    {
        try
        {
            var response = await _httpClient.GetAsync($"ListarPorSolicitante?idSolicitante={idSolicitante}");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<SolicitudGasto>();
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<SolicitudGasto>();
            return JsonSerializer.Deserialize<List<SolicitudGasto>>(json, _jsonOptions) ?? new List<SolicitudGasto>();
        }
        catch
        {
            return new List<SolicitudGasto>();
        }
    }

    public async Task<List<SolicitudGasto>> listarPendientesJefeAsync(int idJefe)
    {
        try
        {
            var response = await _httpClient.GetAsync($"ListarPendientesJefe?idJefe={idJefe}");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<SolicitudGasto>();
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<SolicitudGasto>();
            return JsonSerializer.Deserialize<List<SolicitudGasto>>(json, _jsonOptions) ?? new List<SolicitudGasto>();
        }
        catch
        {
            return new List<SolicitudGasto>();
        }
    }

    public async Task<List<SolicitudGasto>> listarPorCicloAsync(int idCiclo)
    {
        try
        {
            var response = await _httpClient.GetAsync($"ListarPorCiclo?idCiclo={idCiclo}");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<SolicitudGasto>();
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<SolicitudGasto>();
            return JsonSerializer.Deserialize<List<SolicitudGasto>>(json, _jsonOptions) ?? new List<SolicitudGasto>();
        }
        catch
        {
            return new List<SolicitudGasto>();
        }
    }

    public async Task<SolicitudGasto?> evaluarAsync(int idSolicitudGasto, bool aprobado, string comentario, int idJefeEvaluador, int idUsuarioAccion)
    {
        var url = $"Evaluar?idSolicitudGasto={idSolicitudGasto}&aprobado={aprobado.ToString().ToLowerInvariant()}&comentario={Uri.EscapeDataString(comentario ?? "")}&idJefeEvaluador={idJefeEvaluador}&idUsuarioAccion={idUsuarioAccion}";
        var response = await _httpClient.PostAsync(url, null);
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }

        var json = await response.Content.ReadAsStringAsync();
        if (string.IsNullOrEmpty(json) || json == "null")
            return null;
        return JsonSerializer.Deserialize<SolicitudGasto>(json, _jsonOptions);
    }
}
