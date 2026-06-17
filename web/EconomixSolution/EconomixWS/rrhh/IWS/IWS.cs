namespace EconomixWS.UsuarioWS;

public interface IWS <T>
{
    void insertar(T objeto, int idUsuarioAccion);
    void actualizar(T objeto, int idUsuarioAccion);
    void eliminar(int id, int idUsuarioAccion);
    List<T> listar();
}