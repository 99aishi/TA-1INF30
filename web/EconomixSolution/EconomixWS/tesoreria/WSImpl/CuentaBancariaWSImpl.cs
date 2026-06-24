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

    public void insertar(CuentaBancaria obj, int idUsuarioAccion)
    {
        var response = _httpClient.PostAsJsonAsync($"Insertar?idUsuarioAccion={idUsuarioAccion}", obj, _jsonOptions).GetAwaiter().GetResult();
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
    }

    public void actualizar(CuentaBancaria obj, int idUsuarioAccion)
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

    public List<CuentaBancaria> listar()
    {
        try
        {
            var response = _httpClient.GetAsync("Listar").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<CuentaBancaria>();
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<CuentaBancaria>();
            return JsonSerializer.Deserialize<List<CuentaBancaria>>(json, _jsonOptions) ?? new List<CuentaBancaria>();
        }
        catch
        {
            return new List<CuentaBancaria>();
        }
    }

    public CuentaBancaria? obtenerPorId(int id)
    {
        try
        {
            var response = _httpClient.GetAsync($"BuscarPorId?id={id}").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return null;
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return null;
            return JsonSerializer.Deserialize<CuentaBancaria>(json, _jsonOptions);
        }
        catch
        {
            return null;
        }
    }

    public List<CuentaBancaria> listarPorEmpleado(int idEmpleado)
    {
        try
        {
            var response = _httpClient.GetAsync($"ListarPorEmpleado?id={idEmpleado}").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<CuentaBancaria>();
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<CuentaBancaria>();
            return JsonSerializer.Deserialize<List<CuentaBancaria>>(json, _jsonOptions) ?? new List<CuentaBancaria>();
        }
        catch
        {
            return new List<CuentaBancaria>();
        }
    }

    public List<CajaChica> listarCajasChicas(int idCuentaBancaria)
    {
        try
        {
            var response = _httpClient.GetAsync($"ListarCajasChicas?id={idCuentaBancaria}").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<CajaChica>();
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
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
