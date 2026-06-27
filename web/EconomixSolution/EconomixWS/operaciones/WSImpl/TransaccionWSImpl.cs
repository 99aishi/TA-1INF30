using System.Security.Claims;
using System.Text.Json;
using System.Text.Json.Serialization;
using EconomixModel.Model;
using Microsoft.AspNetCore.Http;

namespace EconomixWS.OperacionesWS;

public class TransaccionWSImpl : ITransaccionWS
{
    private readonly HttpClient _httpClient;
    private readonly IHttpContextAccessor _httpContextAccessor;
    private readonly JsonSerializerOptions _jsonOptions;

    public TransaccionWSImpl(HttpClient httpClient, IHttpContextAccessor httpContextAccessor, JsonSerializerOptions jsonOptions)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(httpClient.BaseAddress + "TransaccionWS/");
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

    public async Task insertarAsync(Transaccion obj, int idUsuarioAccion)
    {
        var response = await _httpClient.PostAsJsonAsync($"Insertar?idUsuarioAccion={idUsuarioAccion}", obj, _jsonOptions);
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
    }

    public async Task actualizarAsync(Transaccion obj, int idUsuarioAccion)
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

    public async Task<List<Transaccion>> listarAsync()
    {
        try
        {
            var response = await _httpClient.GetAsync("Listar");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Transaccion>();
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Transaccion>();
            return JsonSerializer.Deserialize<List<Transaccion>>(json, _jsonOptions) ?? new List<Transaccion>();
        }
        catch
        {
            return new List<Transaccion>();
        }
    }

    public async Task<Transaccion?> obtenerPorIdAsync(int id)
    {
        try
        {
            var response = await _httpClient.GetAsync($"BuscarPorId?id={id}");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return null;
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return null;
            return JsonSerializer.Deserialize<Transaccion>(json, _jsonOptions);
        }
        catch
        {
            return null;
        }
    }

    public async Task<List<Transaccion>> listarPorJefeAsync(int idJefe)
    {
        try
        {
            var response = await _httpClient.GetAsync($"ListarPorJefe?idJefe={idJefe}");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Transaccion>();
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Transaccion>();
            return JsonSerializer.Deserialize<List<Transaccion>>(json, _jsonOptions) ?? new List<Transaccion>();
        }
        catch
        {
            return new List<Transaccion>();
        }
    }

}
