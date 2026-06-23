using System.Text.Json;
using EconomixModel.Model;

namespace EconomixWS.TesoreriaWS;

public class TipoCambioWSImpl : ITipoCambioWS
{
    private readonly HttpClient _httpClient;
    private readonly JsonSerializerOptions _jsonOptions;

    public TipoCambioWSImpl(HttpClient httpClient, JsonSerializerOptions jsonOptions)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(httpClient.BaseAddress + "TipoCambioWS/");
        _jsonOptions = jsonOptions;
    }

    public TipoCambio? buscarPorMonedasYFecha(int idMonedaOrigen, int idMonedaDestino, DateTime? fecha = null)
    {
        try
        {
            var fechaParam = fecha.HasValue ? fecha.Value.ToString("yyyy-MM-dd") : DateTime.Today.ToString("yyyy-MM-dd");
            var response = _httpClient.GetAsync($"BuscarPorMonedasYFecha?idMonedaOrigen={idMonedaOrigen}&idMonedaDestino={idMonedaDestino}&fecha={fechaParam}").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return null;
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return null;
            return JsonSerializer.Deserialize<TipoCambio>(json, _jsonOptions);
        }
        catch
        {
            return null;
        }
    }

    public TipoCambio? obtenerPorId(int id)
    {
        try
        {
            var response = _httpClient.GetAsync($"BuscarPorId?id={id}").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return null;
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return null;
            return JsonSerializer.Deserialize<TipoCambio>(json, _jsonOptions);
        }
        catch
        {
            return null;
        }
    }

    public List<TipoCambio> listar()
    {
        try
        {
            var response = _httpClient.GetAsync("Listar").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<TipoCambio>();
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<TipoCambio>();
            return JsonSerializer.Deserialize<List<TipoCambio>>(json, _jsonOptions) ?? new List<TipoCambio>();
        }
        catch
        {
            return new List<TipoCambio>();
        }
    }
}
