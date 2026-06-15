using System.Text.Json;
using System.Text.Json.Serialization;
using EconomixModel.Model;

namespace EconomixWS.UsuarioWS;

public class AreaWSImpl : IAreaWS
{
    private readonly HttpClient _httpClient;
    private static readonly JsonSerializerOptions _jsonOptions = new()
    {
        ReferenceHandler = ReferenceHandler.IgnoreCycles
    };

    public AreaWSImpl(HttpClient httpClient)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(httpClient.BaseAddress + "AreaWS/");
    }
    
    public void insertar(Area area)
    {
        var response = _httpClient.PostAsJsonAsync("Insertar", area, _jsonOptions).GetAwaiter().GetResult();
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
    }

    public void actualizar(Area area)
    {
        var response = _httpClient.PostAsJsonAsync("Actualizar", area, _jsonOptions).GetAwaiter().GetResult();
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
    }

    public void eliminar(int id)
    {
        var response = _httpClient.GetAsync($"Eliminar?id={id}").GetAwaiter().GetResult();
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
    }

    public List<Area> listar()
    {
        var response = _httpClient.GetAsync("ListarAreas").GetAwaiter().GetResult();
        
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
        
        if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
            return new List<Area>();
        
        var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
        if (string.IsNullOrEmpty(json) || json == "null")
            return new List<Area>();
        
        return System.Text.Json.JsonSerializer.Deserialize<List<Area>>(json) ?? new List<Area>();
    }

    public List<Area> listarActivas()
    {
        var response = _httpClient.GetAsync("ListarActivas").GetAwaiter().GetResult();

        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }

        if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
            return new List<Area>();

        var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
        if (string.IsNullOrEmpty(json) || json == "null")
            return new List<Area>();

        return System.Text.Json.JsonSerializer.Deserialize<List<Area>>(json) ?? new List<Area>();
    }

    public int recuperar(int id)
    {
        var response = _httpClient.GetAsync($"Recuperar?id={id}").GetAwaiter().GetResult();
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
        var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
        return System.Text.Json.JsonSerializer.Deserialize<int>(json);
    }

    public Area? obtenerPorId(int id)
    {
        var response = _httpClient.GetAsync($"BuscarPorId?id={id}").GetAwaiter().GetResult();

        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }

        if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
            return null;

        var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
        if (string.IsNullOrEmpty(json) || json == "null")
            return null;

        return System.Text.Json.JsonSerializer.Deserialize<Area>(json);
    }
}