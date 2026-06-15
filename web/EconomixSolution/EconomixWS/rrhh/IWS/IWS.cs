namespace EconomixWS.UsuarioWS;

public interface IWS <T>
{
    void insertar(T objeto);
    void actualizar(T objeto);
    void eliminar(int id);
    List<T> listar();
}