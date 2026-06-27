using System.Security.Claims;
using System.Text.Json;
using EconomixModel.Model;
using Microsoft.AspNetCore.Http;

namespace EconomixWS.TesoreriaWS;

public class MonedaWSImpl : IMonedaWS
{
    private readonly HttpClient _httpClient;
    private readonly IHttpContextAccessor _httpContextAccessor;
    private readonly JsonSerializerOptions _jsonOptions;

    public MonedaWSImpl(HttpClient httpClient, IHttpContextAccessor httpContextAccessor, JsonSerializerOptions jsonOptions)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(httpClient.BaseAddress + "MonedaWS/");
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

    public async Task insertarAsync(Moneda moneda, int idUsuarioAccion)
    {
        var response = await _httpClient.PostAsJsonAsync($"Insertar?idUsuarioAccion={idUsuarioAccion}", moneda, _jsonOptions);
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
    }

    public async Task actualizarAsync(Moneda moneda, int idUsuarioAccion)
    {
        var response = await _httpClient.PostAsJsonAsync($"Actualizar?idUsuarioAccion={idUsuarioAccion}", moneda, _jsonOptions);
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

    public async Task<List<Moneda>> listarAsync()
    {
        try
        {
            var response = await _httpClient.GetAsync("ListarMonedas");

            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Moneda>();

            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Moneda>();

            return System.Text.Json.JsonSerializer.Deserialize<List<Moneda>>(json, _jsonOptions) ?? new List<Moneda>();
        }
        catch
        {
            return new List<Moneda>();
        }
    }

    public async Task<Moneda?> obtenerPorIdAsync(int id)
    {
        try
        {
            var response = await _httpClient.GetAsync($"BuscarPorId?id={id}");

            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return null;

            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return null;

            return System.Text.Json.JsonSerializer.Deserialize<Moneda>(json, _jsonOptions);
        }
        catch
        {
            return null;
        }
    }

    public async Task<List<Moneda>> listarActivasAsync()
    {
        try
        {
            var response = await _httpClient.GetAsync("ListarActivas");

            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Moneda>();

            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Moneda>();

            return System.Text.Json.JsonSerializer.Deserialize<List<Moneda>>(json, _jsonOptions) ?? new List<Moneda>();
        }
        catch
        {
            return new List<Moneda>();
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

    public async Task<List<Moneda>> buscarMonedasAsync(string q)
    {
        try
        {
            var response = await _httpClient.GetAsync($"BuscarMonedas?q={q}");

            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Moneda>();

            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Moneda>();

            return System.Text.Json.JsonSerializer.Deserialize<List<Moneda>>(json, _jsonOptions) ?? new List<Moneda>();
        }
        catch
        {
            return new List<Moneda>();
        }
    }

    public async Task<List<Moneda>> listarPorEstadoAsync(bool activa)
    {
        try
        {
            var response = await _httpClient.GetAsync($"ListarPorEstado?activa={activa}");

            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Moneda>();

            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Moneda>();

            return System.Text.Json.JsonSerializer.Deserialize<List<Moneda>>(json, _jsonOptions) ?? new List<Moneda>();
        }
        catch
        {
            return new List<Moneda>();
        }
    }
}
