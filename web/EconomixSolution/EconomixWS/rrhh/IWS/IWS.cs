namespace EconomixWS.UsuarioWS;

public interface IWS <T>
{
    void insertar(T objeto);
    void actualizar(T objeto);
    void eliminar(T objeto);
    List<T> listar();
}