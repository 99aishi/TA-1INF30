package pe.edu.pucp.economix.dao;

import java.sql.SQLException;
import java.util.List;

public interface IDAO <T> {
     int insertar(T objeto) throws SQLException;
     int modificar(T objeto);
     int eliminar(int id);
     T buscarPorId(int id);
     List<T> listarTodas();
}

