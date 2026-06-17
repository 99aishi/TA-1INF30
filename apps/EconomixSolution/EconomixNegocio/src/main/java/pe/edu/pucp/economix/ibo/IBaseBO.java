package pe.edu.pucp.economix.ibo;

import java.util.List;

public interface IBaseBO <T>{
    int insertar(T objeto, int idUsuarioAccion) throws Exception;
    int modificar(T objeto, int idUsuarioAccion) throws Exception;
    int eliminar(int id, int idUsuarioAccion) throws Exception;
    T buscarPorId(int id) throws Exception;
    List<T> listarTodas() throws Exception;
}
