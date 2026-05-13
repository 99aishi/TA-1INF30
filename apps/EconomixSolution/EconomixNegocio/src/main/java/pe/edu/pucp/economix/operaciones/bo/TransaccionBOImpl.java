package pe.edu.pucp.economix.operaciones.bo;

import pe.edu.pucp.economix.operaciones.boi.ITransaccionBO;
import pe.edu.pucp.economix.operaciones.dao.ITransaccionDAO;
import pe.edu.pucp.economix.operaciones.implement.TransaccionImplement;
import pe.edu.pucp.economix.operaciones.model.TipoTransaccion;
import pe.edu.pucp.economix.operaciones.model.Transaccion;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.tesoreria.bo.CuentaBancariaBOImpl;
import pe.edu.pucp.economix.tesoreria.bo.MonedaBOImpl;
import pe.edu.pucp.economix.tesoreria.boi.ICuentaBancariaBO;
import pe.edu.pucp.economix.tesoreria.boi.IMonedaBO;
import pe.edu.pucp.economix.tesoreria.dao.ICuentaBancariaDAO;
import pe.edu.pucp.economix.tesoreria.implement.CuentaBancariaImplement;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.util.Date;
import java.util.List;

public class TransaccionBOImpl implements ITransaccionBO {

    private final ITransaccionDAO transaccionDAO;
    private final ICuentaBancariaDAO cbDAO;
    private final IMonedaBO monedaBO;
    public TransaccionBOImpl() {
        transaccionDAO= new TransaccionImplement();
        cbDAO = new CuentaBancariaImplement();
        monedaBO= new MonedaBOImpl();
    }


    @Override
    public int insertar(Transaccion transaccion) throws Exception {
        validar(transaccion,false);
        return transaccionDAO.insertar(transaccion);
    }

    @Override
    public int modificar(Transaccion transaccion) throws Exception {
        validar(transaccion,true);
        return transaccionDAO.modificar(transaccion);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id de la transaccion debe ser mayor que cero.");
        }
        return transaccionDAO.eliminar(id);
    }

    @Override
    public Transaccion buscarPorId(int id) throws Exception {
        return transaccionDAO.buscarPorId(id);
    }

    @Override
    public List<Transaccion> listarTodas() throws Exception {
        return transaccionDAO.listarTodas();
    }

    public void validar(Transaccion transaccion, boolean esModificacion) throws Exception{
        if(transaccion==null){
            throw new Exception("La transaccion no puede ser nula.");
        }
        if(esModificacion && transaccion.getIdTransaccion() <= 0){
            throw new Exception("El id de la transaccion es obligatorio para la modificación.");
        }

        validarTipo(transaccion.getTipoTransaccion()); // que no sea nulo
        validarMontoTransaccion(transaccion.getMonto()); // que no sea null
        validarCuentaOrigen(transaccion.getCuentaOrigen());
        validarCuentaDestino(transaccion.getCuentaDestino());
        validarMoneda(transaccion.getMoneda());
        validarFecha(transaccion.getFecha());
    }
    public void validarFecha(Date fecha) throws Exception{
        if(fecha==null){
            throw new Exception("La fecha no puede ser nula.");
        }
    }
    public void validarTipo(TipoTransaccion tipo) throws Exception{
        if(tipo==null)
            throw new Exception("Se debe especificar que tipo de transaccion es.");
    }
    public void validarMontoTransaccion(double monto) throws Exception{
        if(monto==0)
            throw new Exception("El monto de la transaccion no puede ser nulo.");
    }

    public void validarCuentaOrigen(CuentaBancaria cb)    throws  Exception{
        if(cb==null)
            throw new Exception("La cuenta bancaria origen no puede ser nulo.");


        if(cb.getIdCuenta() <= 0){
            throw new Exception("El id de la cuenta bancaria origen debe ser mayor que cero.");
        }
        if(cbDAO.buscarPorId(cb.getIdCuenta())==null){
            throw new Exception("La cuenta bancaria origen no esta registrado.");
        }
    }

    public void validarCuentaDestino(CuentaBancaria cb)    throws  Exception{
        if(cb==null)
            throw new Exception("La cuenta bancaria destino no puede ser nulo.");
        if(cb.getIdCuenta() <= 0){
            throw new Exception("El id de la cuenta bancaria destino debe ser mayor que cero.");
        }
        if(cbDAO.buscarPorId(cb.getIdCuenta())==null){
            throw new Exception("La cuenta bancaria destino no esta registrada.");
        }
    }

    public void validarMoneda(Moneda moneda) throws Exception{

        if(monedaBO.buscarPorId(moneda.getIdMoneda())==null){
            throw new Exception("La moneda seleccionada no esta registrada.");
        }
    }

}
