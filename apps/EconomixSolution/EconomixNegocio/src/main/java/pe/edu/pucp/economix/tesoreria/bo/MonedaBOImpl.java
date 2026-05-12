package pe.edu.pucp.economix.tesoreria.bo;

import pe.edu.pucp.economix.tesoreria.boi.IMonedaBO;
import pe.edu.pucp.economix.tesoreria.dao.IMonedaDAO;
import pe.edu.pucp.economix.tesoreria.implement.MonedaImplement;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.sql.SQLException;
import java.util.List;

public class MonedaBOImpl implements IMonedaBO {

    private final IMonedaDAO daoMoneda;

    public MonedaBOImpl(){
        daoMoneda= new MonedaImplement();
    }
    @Override
    public int insertar(Moneda moneda) throws Exception {
        validar(moneda,false);
        return  daoMoneda.insertar(moneda);
    }

    @Override
    public int modificar(Moneda moneda) throws Exception {
        validar(moneda,true);
        return  daoMoneda.modificar(moneda);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id de la moneda debe ser mayor que cero.");
        }
        return daoMoneda.eliminar(id);
    }

    @Override
    public Moneda buscarPorId(int id) throws Exception {
        if(id<=0){
            throw new Exception("El id de la moneda debe ser mayor que cero.");
        }
        return daoMoneda.buscarPorId(id);
    }

    @Override
    public List<Moneda> listarTodas() throws Exception {
        return List.of();
    }

    public void validar (Moneda moneda, boolean esModificacion) throws Exception{

        if(moneda==null){
            throw new Exception("La moneda no puede ser nula.");

        }
        if (esModificacion && moneda.getIdMoneda() <= 0) {
            throw new Exception("El id de la moneda es obligatoria para la modificación.");
        }

        validarCodigoISO(moneda.getCodigoISO());
        validarSimbolo(moneda.getSimbolo());
    }
    public void validarCodigoISO(String codigoISO) throws Exception{
        if (codigoISO == null || codigoISO.trim().isEmpty()) {
            throw new Exception("El codigoISO de la moneda es obligatorio.");
        }

        codigoISO = codigoISO.trim();

        if (codigoISO.length() < 3) {
            throw new Exception("El codigoISO de la moneda debe tener al menos 3 caracteres.");
        }

        if (codigoISO.length() > 3) {
            throw new Exception("El codigoISO de la moneada no puede exceder los 3 caracteres.");
        }

    }
    public void validarSimbolo(String simbolo)throws Exception{
        if (simbolo == null || simbolo.trim().isEmpty()) {
            throw new Exception("El simbolo de la moneda es obligatorio.");
        }
    }
}
