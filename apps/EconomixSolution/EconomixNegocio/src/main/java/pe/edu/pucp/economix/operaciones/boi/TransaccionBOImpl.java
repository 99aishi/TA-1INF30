package pe.edu.pucp.economix.operaciones.boi;

import pe.edu.pucp.economix.operaciones.ibo.ITransaccionBO;
import pe.edu.pucp.economix.operaciones.idao.ITransaccionDAO;
import pe.edu.pucp.economix.operaciones.daoi.TransaccionDAOImpl;
import pe.edu.pucp.economix.operaciones.model.enums.MedioPago;
import pe.edu.pucp.economix.operaciones.model.enums.TipoTransaccion;
import pe.edu.pucp.economix.operaciones.model.Transaccion;
import pe.edu.pucp.economix.tesoreria.boi.MonedaBOImpl;
import pe.edu.pucp.economix.tesoreria.ibo.IMonedaBO;
import pe.edu.pucp.economix.tesoreria.idao.ICuentaBancariaDAO;
import pe.edu.pucp.economix.tesoreria.daoi.CuentaBancariaDAOImpl;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.util.Date;
import java.util.List;

public class TransaccionBOImpl implements ITransaccionBO {

    private final ITransaccionDAO transaccionDAO;
    private final ICuentaBancariaDAO cbDAO;
    private final IMonedaBO monedaBO;
    public TransaccionBOImpl() {
        transaccionDAO= new TransaccionDAOImpl();
        cbDAO = new CuentaBancariaDAOImpl();
        monedaBO= new MonedaBOImpl();
    }


    @Override
    public int insertar(Transaccion transaccion) throws Exception {
        validar(transaccion,false);
        int resultado= transaccionDAO.insertar(transaccion);

        return resultado;
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
        validarMedioPagoYOperacion(transaccion);
        validarMoneda(transaccion.getMoneda());
        validarFecha(transaccion.getFecha());
    }
    public void validarFecha(Date fecha) throws Exception{
        if(fecha==null){
            throw new Exception("La fecha no puede ser nula.");
        }
    }
    public void validarMedioPagoYOperacion(Transaccion transaccion) throws Exception {
        if (transaccion.getMedioPago() == null) {
            throw new Exception("Debe seleccionar un medio de pago.");
        }

        // Si es un medio digital, el código de operación es obligatorio
        if (transaccion.getMedioPago() == MedioPago.YAPE || transaccion.getMedioPago() == MedioPago.Transferencia) {
            String numOperacion = transaccion.getNumeroOperacionBancaria();
            if (numOperacion == null || numOperacion.trim().isEmpty()) {
                throw new Exception("El código de operación es obligatorio para transferencias y billeteras digitales.");
            }
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
