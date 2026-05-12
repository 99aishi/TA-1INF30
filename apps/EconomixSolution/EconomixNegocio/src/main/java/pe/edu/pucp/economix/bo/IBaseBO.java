package pe.edu.pucp.economix.bo;

import java.sql.SQLException;
import java.util.List;

public interface IBaseBO <T>{
    int insertar(T objeto) throws SQLException;
    int modificar(T objeto) throws SQLException;
    int eliminar(int id) throws SQLException;
    T buscarPorId(int id) throws SQLException;
    List<T> listarTodas() throws SQLException;
}
