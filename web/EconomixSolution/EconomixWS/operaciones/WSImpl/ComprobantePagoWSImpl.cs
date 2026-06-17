using System.Security.Claims;
using System.Text.Json;
using System.Text.Json.Serialization;
using EconomixModel.Model;
using Microsoft.AspNetCore.Http;

namespace EconomixWS.OperacionesWS;

public class ComprobantePagoWSImpl : IComprobantePagoWS
{
    private readonly HttpClient _httpClient;
    private readonly IHttpContextAccessor _httpContextAccessor;
    private static readonly JsonSerializerOptions _jsonOptions = new()
    {
        ReferenceHandler = ReferenceHandler.IgnoreCycles
    };

    public ComprobantePagoWSImpl(HttpClient httpClient, IHttpContextAccessor httpContextAccessor)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(httpClient.BaseAddress + "ComprobantePagoWS/");
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

    public void insertar(ComprobantePago obj, int idUsuarioAccion)
    {
        var response = _httpClient.PostAsJsonAsync($"Insertar?idUsuarioAccion={idUsuarioAccion}", obj, _jsonOptions).GetAwaiter().GetResult();
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
    }

    public void actualizar(ComprobantePago obj, int idUsuarioAccion)
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

    public List<ComprobantePago> listar()
    {
        try
        {
            var response = _httpClient.GetAsync("Listar").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<ComprobantePago>();
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<ComprobantePago>();
            return JsonSerializer.Deserialize<List<ComprobantePago>>(json) ?? new List<ComprobantePago>();
        }
        catch
        {
            return new List<ComprobantePago>();
        }
    }

    public ComprobantePago? obtenerPorId(int id)
    {
        try
        {
            var response = _httpClient.GetAsync($"BuscarPorId?id={id}").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return null;
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return null;
            return JsonSerializer.Deserialize<ComprobantePago>(json);
        }
        catch
        {
            return null;
        }
    }
    public List<ComprobantePago> listarPorSolicitud(int idSolicitud)
    {
        try
        {
            var response = _httpClient.GetAsync($"ListarPorSolicitud?idSolicitud={idSolicitud}").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<ComprobantePago>();
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<ComprobantePago>();
            return JsonSerializer.Deserialize<List<ComprobantePago>>(json) ?? new List<ComprobantePago>();
        }
        catch
        {
            return new List<ComprobantePago>();
        }
    }

}
