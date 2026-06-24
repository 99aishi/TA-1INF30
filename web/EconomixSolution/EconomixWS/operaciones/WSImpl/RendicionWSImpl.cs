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

    public void insertar(Rendicion obj, int idUsuarioAccion)
    {
        var response = _httpClient.PostAsJsonAsync($"Insertar?idUsuarioAccion={idUsuarioAccion}", obj, _jsonOptions).GetAwaiter().GetResult();
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
    }

    public void actualizar(Rendicion obj, int idUsuarioAccion)
    {
        var response = _httpClient.PostAsJsonAsync($"Actualizar?idUsuarioAccion={idUsuarioAccion}", obj, _jsonOptions).GetAwaiter().GetResult();
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
    }

    public void eliminar(int id, int idUsuarioAccion)
    {
        var response = _httpClient.GetAsync($"Eliminar?id={id}&idUsuarioAccion={idUsuarioAccion}").GetAwaiter().GetResult();
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
    }

    public List<Rendicion> listar()
    {
        try
        {
            var response = _httpClient.GetAsync("Listar").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Rendicion>();
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Rendicion>();
            return JsonSerializer.Deserialize<List<Rendicion>>(json, _jsonOptions) ?? new List<Rendicion>();
        }
        catch
        {
            return new List<Rendicion>();
        }
    }

    public Rendicion? obtenerPorId(int id)
    {
        try
        {
            var response = _httpClient.GetAsync($"BuscarPorId?id={id}").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return null;
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return null;
            return JsonSerializer.Deserialize<Rendicion>(json, _jsonOptions);
        }
        catch
        {
            return null;
        }
    }

    public Rendicion? generarRendicionDeCiclo(int idCiclo, int idUsuarioAccion)
    {
        var response = _httpClient.GetAsync($"GenerarRendicionDeCiclo?idCiclo={idCiclo}&idUsuarioAccion={idUsuarioAccion}").GetAwaiter().GetResult();
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
        var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
        if (string.IsNullOrEmpty(json) || json == "null")
            return null;
        return JsonSerializer.Deserialize<Rendicion>(json, _jsonOptions);
    }

    public List<Rendicion> listarPorArea(int idArea)
    {
        try
        {
            var response = _httpClient.GetAsync($"ListarPorArea?idArea={idArea}").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Rendicion>();
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Rendicion>();
            return JsonSerializer.Deserialize<List<Rendicion>>(json, _jsonOptions) ?? new List<Rendicion>();
        }
        catch
        {
            return new List<Rendicion>();
        }
    }

    public void observarRendicion(int idRendicion, string comentario, int idUsuarioAccion)
    {
        var url = $"ObservarRendicion?idRendicion={idRendicion}&comentario={Uri.EscapeDataString(comentario ?? "")}&idUsuarioAccion={idUsuarioAccion}";
        var response = _httpClient.PostAsync(url, null).GetAwaiter().GetResult();
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
    }

    public void aceptarRendicion(int idRendicion, int idUsuarioAccion)
    {
        var url = $"AceptarRendicion?idRendicion={idRendicion}&idUsuarioAccion={idUsuarioAccion}";
        var response = _httpClient.PostAsync(url, null).GetAwaiter().GetResult();
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
    }

    public void denegarRendicion(int idRendicion, string comentario, int idUsuarioAccion)
    {
        var url = $"DenegarRendicion?idRendicion={idRendicion}&comentario={Uri.EscapeDataString(comentario ?? "")}&idUsuarioAccion={idUsuarioAccion}";
        var response = _httpClient.PostAsync(url, null).GetAwaiter().GetResult();
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
    }

    public void reEnviarRendicion(int idRendicion, int idUsuarioAccion)
    {
        var url = $"ReEnviarRendicion?idRendicion={idRendicion}&idUsuarioAccion={idUsuarioAccion}";
        var response = _httpClient.PostAsync(url, null).GetAwaiter().GetResult();
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
    }
}
