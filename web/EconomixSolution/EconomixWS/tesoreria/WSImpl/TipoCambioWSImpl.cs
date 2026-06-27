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

    public async Task<TipoCambio?> buscarPorMonedasYFechaAsync(int idMonedaOrigen, int idMonedaDestino, DateTime? fecha = null)
    {
        try
        {
            var fechaParam = fecha.HasValue ? fecha.Value.ToString("yyyy-MM-dd") : DateTime.Today.ToString("yyyy-MM-dd");
            var response = await _httpClient.GetAsync($"BuscarPorMonedasYFecha?idMonedaOrigen={idMonedaOrigen}&idMonedaDestino={idMonedaDestino}&fecha={fechaParam}");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return null;
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return null;
            return JsonSerializer.Deserialize<TipoCambio>(json, _jsonOptions);
        }
        catch
        {
            return null;
        }
    }

    public async Task<TipoCambio?> obtenerPorIdAsync(int id)
    {
        try
        {
            var response = await _httpClient.GetAsync($"BuscarPorId?id={id}");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return null;
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return null;
            return JsonSerializer.Deserialize<TipoCambio>(json, _jsonOptions);
        }
        catch
        {
            return null;
        }
    }

    public async Task<List<TipoCambio>> listarAsync()
    {
        try
        {
            var response = await _httpClient.GetAsync("Listar");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<TipoCambio>();
            var json = await response.Content.ReadAsStringAsync();
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
