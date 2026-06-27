using System.Security.Claims;
using System.Text.Json;
using System.Text.Json.Serialization;
using EconomixModel.Model;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;

namespace EconomixWS.OperacionesWS;

public class CicloCajaWSImpl : ICicloCajaWS
{
    private readonly HttpClient _httpClient;
    private readonly IHttpContextAccessor _httpContextAccessor;
    private readonly JsonSerializerOptions _jsonOptions;
    private readonly ILogger<CicloCajaWSImpl> _logger;

    public CicloCajaWSImpl(HttpClient httpClient, IHttpContextAccessor httpContextAccessor, JsonSerializerOptions jsonOptions, ILogger<CicloCajaWSImpl> logger)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(httpClient.BaseAddress + "CicloCajaWS/");
        _httpContextAccessor = httpContextAccessor;
        _jsonOptions = jsonOptions;
        _logger = logger;
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

    public async Task insertarAsync(CicloCajaChica obj, int idUsuarioAccion)
    {
        var response = await _httpClient.PostAsJsonAsync($"Insertar?idUsuarioAccion={idUsuarioAccion}", obj, _jsonOptions);
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
    }

    public async Task actualizarAsync(CicloCajaChica obj, int idUsuarioAccion)
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

    public async Task<List<CicloCajaChica>> listarAsync()
    {
        try
        {
            var response = await _httpClient.GetAsync("Listar");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<CicloCajaChica>();
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<CicloCajaChica>();
            return JsonSerializer.Deserialize<List<CicloCajaChica>>(json, _jsonOptions) ?? new List<CicloCajaChica>();
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error al listar ciclos de caja chica");
            return new List<CicloCajaChica>();
        }
    }

    public async Task<CicloCajaChica?> obtenerPorIdAsync(int id)
    {
        try
        {
            var response = await _httpClient.GetAsync($"BuscarPorId?id={id}");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return null;
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return null;
            return JsonSerializer.Deserialize<CicloCajaChica>(json, _jsonOptions);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error al obtener ciclo de caja chica por id {Id}", id);
            return null;
        }
    }

    public async Task cerrarCicloAsync(int id, int idUsuarioAccion)
    {
        var response = await _httpClient.PostAsync($"CerrarCiclo?id={id}&idUsuarioAccion={idUsuarioAccion}", null);
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
    }

}
