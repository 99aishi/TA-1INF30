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
        return _httpClient.GetFromJsonAsync<List<Area>> ("ListarAreas").GetAwaiter().GetResult() ?? new List<Area>();
    }
}