using System.Security.Claims;
using System.Text.Json;
using EconomixModel.Model;
using Microsoft.AspNetCore.Http;

namespace EconomixWS.TesoreriaWS;

public class CuentaBancariaWSImpl : ICuentaBancariaWS
{
    private readonly HttpClient _httpClient;
    private readonly IHttpContextAccessor _httpContextAccessor;
    private readonly JsonSerializerOptions _jsonOptions;

    public CuentaBancariaWSImpl(HttpClient httpClient, IHttpContextAccessor httpContextAccessor, JsonSerializerOptions jsonOptions)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(httpClient.BaseAddress + "CuentaBancariaWS/");
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

    public async Task insertarAsync(CuentaBancaria obj, int idUsuarioAccion)
    {
        var response = await _httpClient.PostAsJsonAsync($"Insertar?idUsuarioAccion={idUsuarioAccion}", obj, _jsonOptions);
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
    }

    public async Task actualizarAsync(CuentaBancaria obj, int idUsuarioAccion)
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

    public async Task<List<CuentaBancaria>> listarAsync()
    {
        try
        {
            var response = await _httpClient.GetAsync("Listar");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<CuentaBancaria>();
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<CuentaBancaria>();
            return JsonSerializer.Deserialize<List<CuentaBancaria>>(json, _jsonOptions) ?? new List<CuentaBancaria>();
        }
        catch
        {
            return new List<CuentaBancaria>();
        }
    }

    public async Task<CuentaBancaria?> obtenerPorIdAsync(int id)
    {
        try
        {
            var response = await _httpClient.GetAsync($"BuscarPorId?id={id}");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return null;
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return null;
            return JsonSerializer.Deserialize<CuentaBancaria>(json, _jsonOptions);
        }
        catch
        {
            return null;
        }
    }

    public async Task<List<CuentaBancaria>> listarPorEmpleadoAsync(int idEmpleado)
    {
        try
        {
            var response = await _httpClient.GetAsync($"ListarPorEmpleado?id={idEmpleado}");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<CuentaBancaria>();
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<CuentaBancaria>();
            return JsonSerializer.Deserialize<List<CuentaBancaria>>(json, _jsonOptions) ?? new List<CuentaBancaria>();
        }
        catch
        {
            return new List<CuentaBancaria>();
        }
    }

    public async Task<List<CajaChica>> listarCajasChicasAsync(int idCuentaBancaria)
    {
        try
        {
            var response = await _httpClient.GetAsync($"ListarCajasChicas?id={idCuentaBancaria}");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<CajaChica>();
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<CajaChica>();
            return JsonSerializer.Deserialize<List<CajaChica>>(json, _jsonOptions) ?? new List<CajaChica>();
        }
        catch
        {
            return new List<CajaChica>();
        }
    }

}
