package pe.edu.pucp.economix.tesoreria.idao;

import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;

import java.sql.SQLException;

public interface ICuentaBancariaDAO {
    int insertar(CuentaBancaria cuentaBancaria, int idUsuarioAccion) throws SQLException;
    int modificar(CuentaBancaria cuentaBancaria, int idUsuarioAccion) throws SQLException;
    int eliminar(int id, int idUsuarioAccion) throws SQLException;
    CuentaBancaria buscarPorId(int id) throws SQLException;
    java.util.List<CuentaBancaria> listarActivas() throws SQLException;
    java.util.List<CuentaBancaria> listarTodas() throws SQLException;
    java.util.List<CuentaBancaria> listarPorEmpleado(int idEmpleado) throws SQLException;
}
