package pe.edu.pucp.economix.tesoreria.ibo;

import pe.edu.pucp.economix.ibo.IBaseBO;
import pe.edu.pucp.economix.tesoreria.model.TipoCambio;

import java.util.Date;

public interface ITipoCambioBO extends IBaseBO<TipoCambio> {
    TipoCambio buscarPorMonedasYFecha(int idMonedaOrigen, int idMonedaDestino, Date fecha) throws Exception;
}
