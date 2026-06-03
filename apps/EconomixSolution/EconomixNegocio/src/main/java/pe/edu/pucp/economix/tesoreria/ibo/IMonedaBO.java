package pe.edu.pucp.economix.tesoreria.ibo;

import pe.edu.pucp.economix.ibo.IBaseBO;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.util.List;

public interface IMonedaBO extends IBaseBO<Moneda> {
    List<Moneda> listarMonedas_X_codigoISO_nombre_simbolo(String busqueda) throws Exception;
    List<Moneda> listarMonedas_X_estado(boolean activa) throws Exception;
}
