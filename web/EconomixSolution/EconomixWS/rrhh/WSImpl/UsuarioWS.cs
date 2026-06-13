namespace EconomixWS.UsuarioWS;

using System.Net.Http.Json;
using System.Security.Claims;
using EconomixModel.Model;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Components.Routing;
using Microsoft.AspNetCore.Components;

public class UsuarioWS : IUsuarioWS
{
    private readonly HttpClient _httpClient;

    public UsuarioWS(HttpClient httpClient)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(httpClient.BaseAddress + "UsuarioWS/");
    }

    public async Task<Usuario?> ValidarCredencialesAsync(LoginRequest request)
    {
        try
        {
            var response = await _httpClient.PostAsJsonAsync("login", request);
            
            
            if (response.IsSuccessStatusCode)
            {
                Usuario? usuario =  await response.Content.ReadFromJsonAsync<Usuario>();
                //Si es que es un usuario valido, entonces 
                return usuario;
            }
            
            return null;
        }
        catch
        {
            return null;
        }
    }
}
