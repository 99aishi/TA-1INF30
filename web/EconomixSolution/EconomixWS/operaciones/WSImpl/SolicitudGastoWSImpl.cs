using System.Security.Claims;
using System.Text.Json;
using System.Text.Json.Serialization;
using EconomixModel.Model;
using Microsoft.AspNetCore.Http;

namespace EconomixWS.OperacionesWS;

public class SolicitudGastoWSImpl : ISolicitudGastoWS
{
    private readonly HttpClient _httpClient;
    private readonly IHttpContextAccessor _httpContextAccessor;
    private static readonly JsonSerializerOptions _jsonOptions = new()
    {
        ReferenceHandler = ReferenceHandler.IgnoreCycles
    };

    public SolicitudGastoWSImpl(HttpClient httpClient, IHttpContextAccessor httpContextAccessor)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(httpClient.BaseAddress + "SolicitudGastoWS/");
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

    public void insertar(SolicitudGasto obj, int idUsuarioAccion)
    {
        var response = _httpClient.PostAsJsonAsync($"Insertar?idUsuarioAccion={idUsuarioAccion}", obj, _jsonOptions).GetAwaiter().GetResult();
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
    }

    public void actualizar(SolicitudGasto obj, int idUsuarioAccion)
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

    public List<SolicitudGasto> listar()
    {
        try
        {
            var response = _httpClient.GetAsync("Listar").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<SolicitudGasto>();
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<SolicitudGasto>();
            return JsonSerializer.Deserialize<List<SolicitudGasto>>(json) ?? new List<SolicitudGasto>();
        }
        catch
        {
            return new List<SolicitudGasto>();
        }
    }

    public SolicitudGasto? obtenerPorId(int id)
    {
        try
        {
            var response = _httpClient.GetAsync($"BuscarPorId?id={id}").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return null;
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return null;
            return JsonSerializer.Deserialize<SolicitudGasto>(json);
        }
        catch
        {
            return null;
        }
    }
    public List<SolicitudGasto> listarPorSolicitante(int idSolicitante)
    {
        try
        {
            var response = _httpClient.GetAsync($"ListarPorSolicitante?idSolicitante={idSolicitante}").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<SolicitudGasto>();
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<SolicitudGasto>();
            return JsonSerializer.Deserialize<List<SolicitudGasto>>(json) ?? new List<SolicitudGasto>();
        }
        catch
        {
            return new List<SolicitudGasto>();
        }
    }

    public List<SolicitudGasto> listarPendientesJefe(int idJefe)
    {
        try
        {
            var response = _httpClient.GetAsync($"ListarPendientesJefe?idJefe={idJefe}").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<SolicitudGasto>();
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<SolicitudGasto>();
            return JsonSerializer.Deserialize<List<SolicitudGasto>>(json) ?? new List<SolicitudGasto>();
        }
        catch
        {
            return new List<SolicitudGasto>();
        }
    }

    public List<SolicitudGasto> listarPorCiclo(int idCiclo)
    {
        try
        {
            var response = _httpClient.GetAsync($"ListarPorCiclo?idCiclo={idCiclo}").GetAwaiter().GetResult();
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<SolicitudGasto>();
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<SolicitudGasto>();
            return JsonSerializer.Deserialize<List<SolicitudGasto>>(json) ?? new List<SolicitudGasto>();
        }
        catch
        {
            return new List<SolicitudGasto>();
        }
    }

    public void evaluar(int idSolicitudGasto, bool aprobado, string comentario, int idJefeEvaluador, string numeroOperacionBancaria, int idUsuarioAccion)
    {
        var url = $"Evaluar?idSolicitudGasto={idSolicitudGasto}&aprobado={aprobado.ToString().ToLowerInvariant()}&comentario={Uri.EscapeDataString(comentario ?? "")}&idJefeEvaluador={idJefeEvaluador}&numeroOperacionBancaria={Uri.EscapeDataString(numeroOperacionBancaria ?? "")}&idUsuarioAccion={idUsuarioAccion}";
        var response = _httpClient.PostAsync(url, null).GetAwaiter().GetResult();
        if (!response.IsSuccessStatusCode)
        {
            var error = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            throw new Exception(error);
        }
    }

}
