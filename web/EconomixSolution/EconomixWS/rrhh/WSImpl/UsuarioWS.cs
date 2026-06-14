namespace EconomixWS.UsuarioWS;

using System.Net.Http.Json;
using System.Security.Claims;
using System.Text.Json;
using EconomixModel.Model;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Components.Routing;
using Microsoft.AspNetCore.Components;
using Microsoft.AspNetCore.Http;

public class UsuarioWS : IUsuarioWS
{
    private readonly HttpClient _httpClient;
    private readonly IHttpContextAccessor _httpContextAccessor;

    public UsuarioWS(HttpClient httpClient, IHttpContextAccessor httpContextAccessor)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(_httpClient.BaseAddress + "UsuarioWS/");
        _httpContextAccessor = httpContextAccessor;
    }

    public Usuario? ValidarCredenciales(LoginRequest request)
    {
        try
        {
            var response = _httpClient.PostAsJsonAsync("login", request).GetAwaiter().GetResult();
            
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
            {
                return null;
            }

            if (response.IsSuccessStatusCode)
            {
                var jsonString = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
                if (string.IsNullOrEmpty(jsonString))
                    return null;

                using var jsonDoc = JsonDocument.Parse(jsonString);
                var root = jsonDoc.RootElement;

                bool isEmpleado = root.TryGetProperty("numeroCelular", out _) ||
                                  root.TryGetProperty("rol", out _) ||
                                  root.TryGetProperty("area", out _) ||
                                  root.TryGetProperty("jefeDirecto", out _);

                if (isEmpleado)
                {
                    return System.Text.Json.JsonSerializer.Deserialize<Empleado>(jsonString);
                }
                else
                {
                    return System.Text.Json.JsonSerializer.Deserialize<Administrador>(jsonString);
                }
            }
            return null;
        }
        catch
        {
            return null;
        }
    }

    public bool IsAuthenticated()
    {
        var user = _httpContextAccessor.HttpContext?.User;
        return user?.Identity?.IsAuthenticated ?? false;
    }

    public Usuario? GetCurrentUser()
    {
        var user = _httpContextAccessor.HttpContext?.User;
        if (user?.Identity?.IsAuthenticated != true)
            return null;

        var nameClaim = user.FindFirst(ClaimTypes.NameIdentifier);
        var roleClaim = user.FindFirst(ClaimTypes.Role);
        var nombreClaim = user.FindFirst("Nombre");
        var paternoClaim = user.FindFirst("ApellidoPaterno");
        var maternoClaim = user.FindFirst("ApellidoMaterno");
        var emailClaim = user.FindFirst(ClaimTypes.Email);
        
        if (nameClaim == null)
            return null;

        string rol = roleClaim?.Value ?? "Empleado";

        if (rol == "Administrador")
        {
            return new Administrador
            {
                UsuarioID = int.Parse(nameClaim.Value),
                Nombres = nombreClaim?.Value ?? "",
                ApellidoPaterno = paternoClaim?.Value ?? "",
                ApellidoMaterno = maternoClaim?.Value ?? "",
                Correo = emailClaim?.Value ?? "",
                Rol = new Rol { Titulo = "Administrador" }
            };
        }
        else
        {
            return new Empleado
            {
                UsuarioID = int.Parse(nameClaim.Value),
                Nombres = nombreClaim?.Value ?? "",
                ApellidoPaterno = paternoClaim?.Value ?? "",
                ApellidoMaterno = maternoClaim?.Value ?? "",
                Correo = emailClaim?.Value ?? "",
                Rol = new Rol { Titulo = rol }
            };
        }
    }

    public void Logout()
    {
        _httpContextAccessor.HttpContext!
            .SignOutAsync(CookieAuthenticationDefaults.AuthenticationScheme)
            .GetAwaiter()
            .GetResult();
    }
}
