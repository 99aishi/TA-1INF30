package pe.edu.pucp.economix.tesoreria.idao;

import pe.edu.pucp.economix.tesoreria.model.TipoCambio;

import java.sql.SQLException;
import java.util.List;

public interface ITipoCambioDAO {
    int insertar(TipoCambio tipoCambio, int idUsuarioAccion) throws SQLException;
    int modificar(TipoCambio tipoCambio, int idUsuarioAccion) throws SQLException;
    int eliminar(int id, int idUsuarioAccion) throws SQLException;
    TipoCambio buscarPorId(int id) throws SQLException;
    List<TipoCambio> listarTodas() throws SQLException;
    TipoCambio buscarPorMonedasYFecha(int idMonedaOrigen, int idMonedaDestino, java.sql.Date fecha) throws SQLException;
}
