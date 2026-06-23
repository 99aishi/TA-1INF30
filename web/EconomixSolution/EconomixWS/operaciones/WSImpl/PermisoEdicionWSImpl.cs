using System.Security.Claims;
using System.Text.Json;
using EconomixModel.Model;
using Microsoft.AspNetCore.Http;

namespace EconomixWS.OperacionesWS;

public class PermisoEdicionWSImpl : IPermisoEdicionWS
{
    private readonly HttpClient _httpClient;
    private readonly IHttpContextAccessor _httpContextAccessor;
    private readonly JsonSerializerOptions _jsonOptions;

    public PermisoEdicionWSImpl(HttpClient httpClient, IHttpContextAccessor httpContextAccessor, JsonSerializerOptions jsonOptions)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(httpClient.BaseAddress + "PermisoEdicionWS/");
        _httpContextAccessor = httpContextAccessor;
        _jsonOptions = jsonOptions;
    }

    private int ObtenerIdUsuarioAccion()
    {
        var user = _httpContextAccessor.HttpContext?.User;
        if (user?.Identity?.IsAuthenticated != true)
            throw new UnauthorizedAccessException("No hay una sesion activa.");

        var nameClaim = user.FindFirst(ClaimTypes.NameIdentifier)
            ?? throw new UnauthorizedAccessException("No se encontro el identificador de usuario en la sesion.");

        return int.Parse(nameClaim.Value);
    }

    public void Solicitar(PermisoEdicion permiso, int idUsuarioAccion)
    {
        var response = _httpClient.PostAsJsonAsync($"Solicitar?idUsuarioAccion={idUsuarioAccion}", permiso, _jsonOptions).GetAwaiter().GetResult();
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
    }

    public void Otorgar(int idPermiso, int idAutorizador, string motivoAutorizacion, int idUsuarioAccion)
    {
        var url = $"Otorgar?idPermiso={idPermiso}&idAutorizador={idAutorizador}&motivoAutorizacion={Uri.EscapeDataString(motivoAutorizacion ?? "")}&idUsuarioAccion={idUsuarioAccion}";
        var response = _httpClient.PostAsync(url, null).GetAwaiter().GetResult();
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
    }

    public void Revocar(int idPermiso, int idUsuarioAccion)
    {
        var response = _httpClient.PostAsync($"Revocar?idPermiso={idPermiso}&idUsuarioAccion={idUsuarioAccion}", null).GetAwaiter().GetResult();
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
    }

    public List<PermisoEdicion> ListarPendientes(int idAutorizador)
    {
        try
        {
            var response = _httpClient.GetAsync($"ListarPendientes?idAutorizador={idAutorizador}").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<PermisoEdicion>();
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<PermisoEdicion>();
            return JsonSerializer.Deserialize<List<PermisoEdicion>>(json, _jsonOptions) ?? new List<PermisoEdicion>();
        }
        catch
        {
            return new List<PermisoEdicion>();
        }
    }

    public List<PermisoEdicion> ListarEnExcepcion()
    {
        try
        {
            var response = _httpClient.GetAsync("ListarEnExcepcion").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<PermisoEdicion>();
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<PermisoEdicion>();
            return JsonSerializer.Deserialize<List<PermisoEdicion>>(json, _jsonOptions) ?? new List<PermisoEdicion>();
        }
        catch
        {
            return new List<PermisoEdicion>();
        }
    }
}
