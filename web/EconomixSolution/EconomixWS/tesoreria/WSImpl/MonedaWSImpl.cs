using System.Text.Json;
using System.Text.Json.Serialization;
using EconomixModel.Model;

namespace EconomixWS.TesoreriaWS;

public class MonedaWSImpl : IMonedaWS
{
    private readonly HttpClient _httpClient;
    private static readonly JsonSerializerOptions _jsonOptions = new()
    {
        ReferenceHandler = ReferenceHandler.IgnoreCycles
    };

    public MonedaWSImpl(HttpClient httpClient)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(httpClient.BaseAddress + "MonedaWS/");
    }

    public void insertar(Moneda moneda)
    {
        var response = _httpClient.PostAsJsonAsync("Insertar", moneda, _jsonOptions).GetAwaiter().GetResult();
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
    }

    public void actualizar(Moneda moneda)
    {
        var response = _httpClient.PostAsJsonAsync("Actualizar", moneda, _jsonOptions).GetAwaiter().GetResult();
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

    public List<Moneda> listar()
    {
        try
        {
            var response = _httpClient.GetAsync("ListarMonedas").GetAwaiter().GetResult();

            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Moneda>();

            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Moneda>();

            return System.Text.Json.JsonSerializer.Deserialize<List<Moneda>>(json) ?? new List<Moneda>();
        }
        catch
        {
            return new List<Moneda>();
        }
    }

    public Moneda? obtenerPorId(int id)
    {
        try
        {
            var response = _httpClient.GetAsync($"BuscarPorId?id={id}").GetAwaiter().GetResult();

            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return null;

            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return null;

            return System.Text.Json.JsonSerializer.Deserialize<Moneda>(json);
        }
        catch
        {
            return null;
        }
    }

    public List<Moneda> listarActivas()
    {
        try
        {
            var response = _httpClient.GetAsync("ListarActivas").GetAwaiter().GetResult();

            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Moneda>();

            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Moneda>();

            return System.Text.Json.JsonSerializer.Deserialize<List<Moneda>>(json) ?? new List<Moneda>();
        }
        catch
        {
            return new List<Moneda>();
        }
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

    public List<Moneda> buscarMonedas(string q)
    {
        try
        {
            var response = _httpClient.GetAsync($"BuscarMonedas?q={q}").GetAwaiter().GetResult();

            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Moneda>();

            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Moneda>();

            return System.Text.Json.JsonSerializer.Deserialize<List<Moneda>>(json) ?? new List<Moneda>();
        }
        catch
        {
            return new List<Moneda>();
        }
    }

    public List<Moneda> listarPorEstado(bool activa)
    {
        try
        {
            var response = _httpClient.GetAsync($"ListarPorEstado?activa={activa}").GetAwaiter().GetResult();

            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Moneda>();

            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Moneda>();

            return System.Text.Json.JsonSerializer.Deserialize<List<Moneda>>(json) ?? new List<Moneda>();
        }
        catch
        {
            return new List<Moneda>();
        }
    }
}
