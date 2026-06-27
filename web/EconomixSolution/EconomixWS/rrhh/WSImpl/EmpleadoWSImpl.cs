using System.Security.Claims;
using System.Text.Json;
using EconomixModel.Model;
using Microsoft.AspNetCore.Http;

namespace EconomixWS.UsuarioWS;

public class EmpleadoWSImpl : IEmpleadoWS
{
    private readonly HttpClient _httpClient;
    private readonly IHttpContextAccessor _httpContextAccessor;
    private readonly JsonSerializerOptions _jsonOptions;

    public EmpleadoWSImpl(HttpClient httpClient, IHttpContextAccessor httpContextAccessor, JsonSerializerOptions jsonOptions)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(httpClient.BaseAddress + "EmpleadoWS/");
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

    public async Task insertarAsync(Empleado obj, int idUsuarioAccion)
    {
        var response = await _httpClient.PostAsJsonAsync($"Insertar?idUsuarioAccion={idUsuarioAccion}", obj, _jsonOptions);
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
    }

    public async Task actualizarAsync(Empleado obj, int idUsuarioAccion)
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

    public async Task<List<Empleado>> listarAsync()
    {
        var response = await _httpClient.GetAsync("ListarEmpleados");
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception($"Error al listar empleados: {error}");
        }
        if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
            return new List<Empleado>();
        var json = await response.Content.ReadAsStringAsync();
        if (string.IsNullOrEmpty(json) || json == "null")
            return new List<Empleado>();
        return JsonSerializer.Deserialize<List<Empleado>>(json, _jsonOptions) ?? new List<Empleado>();
    }

    public async Task<Empleado?> obtenerPorIdAsync(int id)
    {
        try
        {
            var response = await _httpClient.GetAsync($"BuscarPorId?id={id}");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return null;
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return null;
            return JsonSerializer.Deserialize<Empleado>(json, _jsonOptions);
        }
        catch
        {
            return null;
        }
    }
    public async Task<List<Empleado>> listarPorNombreApellidoAsync(string q)
    {
        try
        {
            var response = await _httpClient.GetAsync($"ListarPorNombreApellido?q={q}");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Empleado>();
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Empleado>();
            return JsonSerializer.Deserialize<List<Empleado>>(json, _jsonOptions) ?? new List<Empleado>();
        }
        catch
        {
            return new List<Empleado>();
        }
    }

    public async Task<List<Empleado>> listarPorAreaAsync(int idArea)
    {
        try
        {
            var response = await _httpClient.GetAsync($"ListarPorArea?idArea={idArea}");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Empleado>();
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Empleado>();
            return JsonSerializer.Deserialize<List<Empleado>>(json, _jsonOptions) ?? new List<Empleado>();
        }
        catch
        {
            return new List<Empleado>();
        }
    }

}
