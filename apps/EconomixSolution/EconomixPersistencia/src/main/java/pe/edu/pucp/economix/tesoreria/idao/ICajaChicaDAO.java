package pe.edu.pucp.economix.tesoreria.idao;

import pe.edu.pucp.economix.tesoreria.model.CajaChica;

import java.sql.SQLException;

public interface ICajaChicaDAO {
    int insertar(CajaChica cajaChica, int idUsuarioAccion) throws SQLException;
    int modificar(CajaChica cajaChica, int idUsuarioAccion) throws SQLException;
    int eliminar(int id, int idUsuarioAccion) throws SQLException;
    CajaChica buscarPorId(int id) throws SQLException;
    java.util.List<CajaChica> listarActivas() throws SQLException;
    java.util.List<CajaChica> listarTodas() throws SQLException;
}
