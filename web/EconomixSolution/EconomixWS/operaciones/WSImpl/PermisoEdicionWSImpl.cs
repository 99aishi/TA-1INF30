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

    public async Task SolicitarAsync(PermisoEdicion permiso, int idUsuarioAccion)
    {
        var response = await _httpClient.PostAsJsonAsync($"Solicitar?idUsuarioAccion={idUsuarioAccion}", permiso, _jsonOptions);
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
    }

    public async Task OtorgarAsync(int idPermiso, int idAutorizador, string motivoAutorizacion, int idUsuarioAccion)
    {
        var url = $"Otorgar?idPermiso={idPermiso}&idAutorizador={idAutorizador}&motivoAutorizacion={Uri.EscapeDataString(motivoAutorizacion ?? "")}&idUsuarioAccion={idUsuarioAccion}";
        var response = await _httpClient.PostAsync(url, null);
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
    }

    public async Task RevocarAsync(int idPermiso, int idUsuarioAccion)
    {
        var response = await _httpClient.PostAsync($"Revocar?idPermiso={idPermiso}&idUsuarioAccion={idUsuarioAccion}", null);
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }
    }

    public async Task<List<PermisoEdicion>> ListarPendientesAsync(int idAutorizador)
    {
        try
        {
            var response = await _httpClient.GetAsync($"ListarPendientes?idAutorizador={idAutorizador}");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<PermisoEdicion>();
            var json = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<PermisoEdicion>();
            return JsonSerializer.Deserialize<List<PermisoEdicion>>(json, _jsonOptions) ?? new List<PermisoEdicion>();
        }
        catch
        {
            return new List<PermisoEdicion>();
        }
    }

    public async Task<List<PermisoEdicion>> ListarEnExcepcionAsync()
    {
        try
        {
            var response = await _httpClient.GetAsync("ListarEnExcepcion");
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<PermisoEdicion>();
            var json = await response.Content.ReadAsStringAsync();
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
