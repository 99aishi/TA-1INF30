package pe.edu.pucp.economix.tesoreria.idao;

import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.sql.SQLException;
import java.util.List;

public interface IMonedaDAO {
    int insertar(Moneda moneda, int idUsuarioAccion) throws SQLException;
    int modificar(Moneda moneda, int idUsuarioAccion) throws SQLException;
    int eliminar(int id, int idUsuarioAccion) throws SQLException;
    Moneda buscarPorId(int id) throws SQLException;
    List<Moneda> listarTodas() throws SQLException;
    List<Moneda> listarMonedas_X_codigoISO_nombre_simbolo(String busqueda) throws SQLException;
    List<Moneda> listarMonedas_X_estado(boolean activa) throws SQLException;
    List<Moneda> listarActivas() throws SQLException;
    int recuperar(int id, int idUsuarioAccion) throws SQLException;
}
