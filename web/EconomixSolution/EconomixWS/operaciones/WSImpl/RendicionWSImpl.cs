using System.Security.Claims;
using System.Text.Json;
using EconomixModel.Model;
using Microsoft.AspNetCore.Http;

namespace EconomixWS.OperacionesWS;

public class RendicionWSImpl : IRendicionWS
{
    private readonly HttpClient _httpClient;
    private readonly IHttpContextAccessor _httpContextAccessor;
    private readonly JsonSerializerOptions _jsonOptions;

    public RendicionWSImpl(HttpClient httpClient, IHttpContextAccessor httpContextAccessor, JsonSerializerOptions jsonOptions)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(httpClient.BaseAddress + "RendicionWS/");
        _httpContextAccessor = httpContextAccessor;
        _jsonOptions = jsonOptions;
    }

    public async Task insertarAsync(Rendicion obj, int idUsuarioAccion)
    {
        var response = await _httpClient.PostAsJsonAsync($"Insertar?idUsuarioAccion={idUsuarioAccion}", obj, _jsonOptions);
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
    }

    public async Task actualizarAsync(Rendicion obj, int idUsuarioAccion)
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

    public async Task<List<Rendicion>> listarAsync()
    {
        try
        {
            var response = await _httpClient.GetAsync("Listar");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Rendicion>();
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Rendicion>();
            return JsonSerializer.Deserialize<List<Rendicion>>(json, _jsonOptions) ?? new List<Rendicion>();
        }
        catch
        {
            return new List<Rendicion>();
        }
    }

    public async Task<Rendicion?> obtenerPorIdAsync(int id)
    {
        try
        {
            var response = await _httpClient.GetAsync($"BuscarPorId?id={id}");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return null;
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return null;
            return JsonSerializer.Deserialize<Rendicion>(json, _jsonOptions);
        }
        catch
        {
            return null;
        }
    }

    public async Task<Rendicion?> generarRendicionDeCicloAsync(int idCiclo, int idUsuarioAccion)
    {
        var response = await _httpClient.GetAsync($"GenerarRendicionDeCiclo?idCiclo={idCiclo}&idUsuarioAccion={idUsuarioAccion}");
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
        var json = await response.Content.ReadAsStringAsync();
        if (string.IsNullOrEmpty(json) || json == "null")
            return null;
        return JsonSerializer.Deserialize<Rendicion>(json, _jsonOptions);
    }

    public async Task<List<Rendicion>> listarPorAreaAsync(int idArea)
    {
        try
        {
            var response = await _httpClient.GetAsync($"ListarPorArea?idArea={idArea}");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Rendicion>();
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Rendicion>();
            return JsonSerializer.Deserialize<List<Rendicion>>(json, _jsonOptions) ?? new List<Rendicion>();
        }
        catch
        {
            return new List<Rendicion>();
        }
    }

    public async Task observarRendicionAsync(int idRendicion, string comentario, int idUsuarioAccion)
    {
        var url = $"ObservarRendicion?idRendicion={idRendicion}&comentario={Uri.EscapeDataString(comentario ?? "")}&idUsuarioAccion={idUsuarioAccion}";
        var response = await _httpClient.PostAsync(url, null);
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
    }

    public async Task aceptarRendicionAsync(int idRendicion, int idUsuarioAccion)
    {
        var url = $"AceptarRendicion?idRendicion={idRendicion}&idUsuarioAccion={idUsuarioAccion}";
        var response = await _httpClient.PostAsync(url, null);
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
    }

    public async Task denegarRendicionAsync(int idRendicion, string comentario, int idUsuarioAccion)
    {
        var url = $"DenegarRendicion?idRendicion={idRendicion}&comentario={Uri.EscapeDataString(comentario ?? "")}&idUsuarioAccion={idUsuarioAccion}";
        var response = await _httpClient.PostAsync(url, null);
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
    }

    public async Task reEnviarRendicionAsync(int idRendicion, int idUsuarioAccion)
    {
        var url = $"ReEnviarRendicion?idRendicion={idRendicion}&idUsuarioAccion={idUsuarioAccion}";
        var response = await _httpClient.PostAsync(url, null);
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
    }
}
