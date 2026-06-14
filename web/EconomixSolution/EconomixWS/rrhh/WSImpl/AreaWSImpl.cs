using EconomixModel.Model;

namespace EconomixWS.UsuarioWS;

public class AreaWSImpl : IAreaWS
{
    
    private readonly HttpClient _httpClient;

    public AreaWSImpl(HttpClient httpClient)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(httpClient.BaseAddress + "AreaWS/");
    }


    public void insertar(Area area)
    {
        _httpClient.PostAsJsonAsync("Insertar",area).GetAwaiter().GetResult();
    }

    public void actualizar(Area area)
    {
         _httpClient.PostAsJsonAsync("Actualizar", area).GetAwaiter().GetResult();
    }

    public void eliminar(Area area)
    {
        _httpClient.PostAsJsonAsync("Eliminar", area).GetAwaiter().GetResult();
    }

    public List<Area> listar()
    {
        try
        {
            var response = _httpClient.GetAsync("ListarAreas").GetAwaiter().GetResult();
            
            if (response.StatusCode == System.Net.HttpStatusCode.NoContent)
                return new List<Area>();
            
            var json = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            if (string.IsNullOrEmpty(json) || json == "null")
                return new List<Area>();
            
            return System.Text.Json.JsonSerializer.Deserialize<List<Area>>(json) ?? new List<Area>();
        }
        catch
        {
            return new List<Area>();
        }
    }
}