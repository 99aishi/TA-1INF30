using System.Security.Claims;
using System.Text.Json;
using EconomixModel.Model;
using Microsoft.AspNetCore.Http;

namespace EconomixWS.UsuarioWS;

public class AreaWSImpl : IAreaWS
{
    private readonly HttpClient _httpClient;
    private readonly IHttpContextAccessor _httpContextAccessor;
    private readonly JsonSerializerOptions _jsonOptions;

    public AreaWSImpl(HttpClient httpClient, IHttpContextAccessor httpContextAccessor, JsonSerializerOptions jsonOptions)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(httpClient.BaseAddress + "AreaWS/");
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
    
    public async Task insertarAsync(Area area, int idUsuarioAccion)
    {
        var response = await _httpClient.PostAsJsonAsync($"Insertar?idUsuarioAccion={idUsuarioAccion}", area, _jsonOptions);
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
    }

    public async Task actualizarAsync(Area area, int idUsuarioAccion)
    {
        var response = await _httpClient.PostAsJsonAsync($"Actualizar?idUsuarioAccion={idUsuarioAccion}", area, _jsonOptions);
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

    public async Task<List<Area>> listarAsync()
    {
        try
        {
            var response = await _httpClient.GetAsync("ListarAreas");

            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Area>();

            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Area>();

            return System.Text.Json.JsonSerializer.Deserialize<List<Area>>(json, _jsonOptions) ?? new List<Area>();
        }
        catch
        {
            return new List<Area>();
        }
    }

    public async Task<List<Area>> listarActivasAsync()
    {
        try
        {
            var response = await _httpClient.GetAsync("ListarActivas");

            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Area>();

            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Area>();

            return System.Text.Json.JsonSerializer.Deserialize<List<Area>>(json, _jsonOptions) ?? new List<Area>();
        }
        catch
        {
            return new List<Area>();
        }
    }

    public async Task<int> recuperarAsync(int id, int idUsuarioAccion)
    {
        var response = await _httpClient.GetAsync($"Recuperar?id={id}&idUsuarioAccion={idUsuarioAccion}");
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
        var json = await response.Content.ReadAsStringAsync();
        return System.Text.Json.JsonSerializer.Deserialize<int>(json, _jsonOptions);
    }

    public async Task<Area?> obtenerPorIdAsync(int id)
    {
        try
        {
            var response = await _httpClient.GetAsync($"BuscarPorId?id={id}");

            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return null;

            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return null;

            return System.Text.Json.JsonSerializer.Deserialize<Area>(json, _jsonOptions);
        }
        catch
        {
            return null;
        }
    }
}
