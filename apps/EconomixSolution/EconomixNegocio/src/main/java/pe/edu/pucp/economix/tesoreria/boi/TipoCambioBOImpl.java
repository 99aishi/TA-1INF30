package pe.edu.pucp.economix.tesoreria.boi;

import pe.edu.pucp.economix.tesoreria.ibo.ITipoCambioBO;
import pe.edu.pucp.economix.tesoreria.idao.ITipoCambioDAO;
import pe.edu.pucp.economix.tesoreria.daoi.TipoCambioDAOImpl;
import pe.edu.pucp.economix.tesoreria.model.TipoCambio;

import java.sql.Date;
import java.util.List;

public class TipoCambioBOImpl implements ITipoCambioBO {

    private final ITipoCambioDAO tipoCambioDAO;

    public TipoCambioBOImpl() {
        this.tipoCambioDAO = new TipoCambioDAOImpl();
    }

    private void validarIdUsuarioAccion(int idUsuarioAccion) throws Exception {
        if (idUsuarioAccion <= 0) {
            throw new Exception("El usuario de acción debe ser mayor que cero.");
        }
    }

    private void validar(TipoCambio tipoCambio, boolean esModificacion) throws Exception {
        if (tipoCambio == null) {
            throw new Exception("El tipo de cambio no puede ser nulo.");
        }
        if (esModificacion && tipoCambio.getIdTipoCambio() <= 0) {
            throw new Exception("El id del tipo de cambio es obligatorio para la modificación.");
        }
        if (tipoCambio.getMonedaOrigen() == null || tipoCambio.getMonedaOrigen().getIdMoneda() <= 0) {
            throw new Exception("La moneda origen es obligatoria.");
        }
        if (tipoCambio.getMonedaDestino() == null || tipoCambio.getMonedaDestino().getIdMoneda() <= 0) {
            throw new Exception("La moneda destino es obligatoria.");
        }
        if (tipoCambio.getValor() <= 0) {
            throw new Exception("El valor del tipo de cambio debe ser mayor que cero.");
        }
        if (tipoCambio.getFecha() == null) {
            throw new Exception("La fecha del tipo de cambio es obligatoria.");
        }
    }

    @Override
    public int insertar(TipoCambio tipoCambio, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(tipoCambio, false);
        return tipoCambioDAO.insertar(tipoCambio, idUsuarioAccion);
    }

    @Override
    public int modificar(TipoCambio tipoCambio, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(tipoCambio, true);
        return tipoCambioDAO.modificar(tipoCambio, idUsuarioAccion);
    }

    @Override
    public int eliminar(int id, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        if (id <= 0) {
            throw new Exception("El id del tipo de cambio debe ser mayor que cero.");
        }
        return tipoCambioDAO.eliminar(id, idUsuarioAccion);
    }

    @Override
    public TipoCambio buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del tipo de cambio debe ser mayor que cero.");
        }
        return tipoCambioDAO.buscarPorId(id);
    }

    @Override
    public List<TipoCambio> listarTodas() throws Exception {
        return tipoCambioDAO.listarTodas();
    }

    @Override
    public TipoCambio buscarPorMonedasYFecha(int idMonedaOrigen, int idMonedaDestino, java.util.Date fecha) throws Exception {
        if (idMonedaOrigen <= 0) {
            throw new Exception("El id de la moneda origen debe ser mayor que cero.");
        }
        if (idMonedaDestino <= 0) {
            throw new Exception("El id de la moneda destino debe ser mayor que cero.");
        }
        if (fecha == null) {
            throw new Exception("La fecha es obligatoria.");
        }
        return tipoCambioDAO.buscarPorMonedasYFecha(idMonedaOrigen, idMonedaDestino, new Date(fecha.getTime()));
    }
}
