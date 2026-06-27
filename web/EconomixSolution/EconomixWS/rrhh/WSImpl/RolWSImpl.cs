using System.Security.Claims;
using System.Text.Json;
using EconomixModel.Model;
using Microsoft.AspNetCore.Http;

namespace EconomixWS.UsuarioWS;

public class RolWSImpl : IRolWS
{
    private readonly HttpClient _httpClient;
    private readonly IHttpContextAccessor _httpContextAccessor;
    private readonly JsonSerializerOptions _jsonOptions;

    public RolWSImpl(HttpClient httpClient, IHttpContextAccessor httpContextAccessor, JsonSerializerOptions jsonOptions)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(httpClient.BaseAddress + "RolWS/");
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

    public async Task insertarAsync(Rol rol, int idUsuarioAccion)
    {
        var response = await _httpClient.PostAsJsonAsync($"Insertar?idUsuarioAccion={idUsuarioAccion}", rol, _jsonOptions);
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
    }

    public async Task actualizarAsync(Rol rol, int idUsuarioAccion)
    {
        var response = await _httpClient.PostAsJsonAsync($"Actualizar?idUsuarioAccion={idUsuarioAccion}", rol, _jsonOptions);
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

    public async Task<List<Rol>> listarAsync()
    {
        try
        {
            var response = await _httpClient.GetAsync("ListarRoles");

            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Rol>();

            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Rol>();

            return System.Text.Json.JsonSerializer.Deserialize<List<Rol>>(json, _jsonOptions) ?? new List<Rol>();
        }
        catch
        {
            return new List<Rol>();
        }
    }

    public async Task<Rol?> obtenerPorIdAsync(int id)
    {
        try
        {
            var response = await _httpClient.GetAsync($"BuscarPorId?id={id}");

            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return null;

            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return null;

            return System.Text.Json.JsonSerializer.Deserialize<Rol>(json, _jsonOptions);
        }
        catch
        {
            return null;
        }
    }

    public async Task<List<Rol>> listarPorAreaAsync(int idArea)
    {
        var response = await _httpClient.GetAsync($"ListarPorArea?idArea={idArea}");
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }

        if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
            return new List<Rol>();

        var json = await response.Content.ReadAsStringAsync();
        if (string.IsNullOrEmpty(json) || json == "null")
            return new List<Rol>();

        return System.Text.Json.JsonSerializer.Deserialize<List<Rol>>(json, _jsonOptions) ?? new List<Rol>();
    }
}
