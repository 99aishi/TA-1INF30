using System.Security.Claims;
using System.Text.Json;
using System.Text.Json.Serialization;
using EconomixModel.Model;
using Microsoft.AspNetCore.Http;

namespace EconomixWS.UsuarioWS;

public class EmpleadoWSImpl : IEmpleadoWS
{
    private readonly HttpClient _httpClient;
    private readonly IHttpContextAccessor _httpContextAccessor;
    private static readonly JsonSerializerOptions _jsonOptions = new()
    {
        ReferenceHandler = ReferenceHandler.IgnoreCycles
    };

    public EmpleadoWSImpl(HttpClient httpClient, IHttpContextAccessor httpContextAccessor)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(httpClient.BaseAddress + "EmpleadoWS/");
        _httpContextAccessor = httpContextAccessor;
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

    public void insertar(Empleado obj, int idUsuarioAccion)
    {
        var response = _httpClient.PostAsJsonAsync($"Insertar?idUsuarioAccion={idUsuarioAccion}", obj, _jsonOptions).GetAwaiter().GetResult();
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
    }

    public void actualizar(Empleado obj, int idUsuarioAccion)
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

    public List<Empleado> listar()
    {
        try
        {
            var response = _httpClient.GetAsync("Listar").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Empleado>();
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Empleado>();
            return JsonSerializer.Deserialize<List<Empleado>>(json) ?? new List<Empleado>();
        }
        catch
        {
            return new List<Empleado>();
        }
    }

    public Empleado? obtenerPorId(int id)
    {
        try
        {
            var response = _httpClient.GetAsync($"BuscarPorId?id={id}").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return null;
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return null;
            return JsonSerializer.Deserialize<Empleado>(json);
        }
        catch
        {
            return null;
        }
    }
    public List<Empleado> listarPorNombreApellido(string q)
    {
        try
        {
            var response = _httpClient.GetAsync($"ListarPorNombreApellido?q={q}").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Empleado>();
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Empleado>();
            return JsonSerializer.Deserialize<List<Empleado>>(json) ?? new List<Empleado>();
        }
        catch
        {
            return new List<Empleado>();
        }
    }

}
