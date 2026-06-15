package pe.edu.pucp.economix.tesoreria.idao;

import pe.edu.pucp.economix.idao.IDAO;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.sql.SQLException;
import java.util.List;

public interface IMonedaDAO extends IDAO <Moneda> {
  List<Moneda> listarMonedas_X_codigoISO_nombre_simbolo(String busqueda) throws SQLException ;
  List<Moneda> listarMonedas_X_estado(boolean activa) throws SQLException;
  List<Moneda> listarActivas() throws SQLException;
  int recuperar(int id) throws SQLException;
}
