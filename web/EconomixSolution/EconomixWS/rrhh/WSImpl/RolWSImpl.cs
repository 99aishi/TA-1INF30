using System.Text.Json;
using System.Text.Json.Serialization;
using EconomixModel.Model;

namespace EconomixWS.UsuarioWS;

public class RolWSImpl : IRolWS
{
    private readonly HttpClient _httpClient;
    private static readonly JsonSerializerOptions _jsonOptions = new()
    {
        ReferenceHandler = ReferenceHandler.IgnoreCycles
    };

    public RolWSImpl(HttpClient httpClient)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(httpClient.BaseAddress + "RolWS/");
    }

    public void insertar(Rol rol)
    {
        var response = _httpClient.PostAsJsonAsync("Insertar", rol, _jsonOptions).GetAwaiter().GetResult();
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
    }

    public void actualizar(Rol rol)
    {
        var response = _httpClient.PostAsJsonAsync("Actualizar", rol, _jsonOptions).GetAwaiter().GetResult();
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

    public List<Rol> listar()
    {
        try
        {
            var response = _httpClient.GetAsync("ListarRoles").GetAwaiter().GetResult();

            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Rol>();

            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Rol>();

            return System.Text.Json.JsonSerializer.Deserialize<List<Rol>>(json) ?? new List<Rol>();
        }
        catch
        {
            return new List<Rol>();
        }
    }

    public Rol? obtenerPorId(int id)
    {
        try
        {
            var response = _httpClient.GetAsync($"BuscarPorId?id={id}").GetAwaiter().GetResult();

            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return null;

            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return null;

            return System.Text.Json.JsonSerializer.Deserialize<Rol>(json);
        }
        catch
        {
            return null;
        }
    }
}
