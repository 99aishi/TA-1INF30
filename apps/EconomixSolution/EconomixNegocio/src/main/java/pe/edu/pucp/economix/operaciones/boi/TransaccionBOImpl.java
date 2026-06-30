package pe.edu.pucp.economix.operaciones.boi;

import pe.edu.pucp.economix.operaciones.ibo.ITransaccionBO;
import pe.edu.pucp.economix.operaciones.idao.ITransaccionDAO;
import pe.edu.pucp.economix.operaciones.daoi.TransaccionDAOImpl;
import pe.edu.pucp.economix.operaciones.model.enums.MedioPago;
import pe.edu.pucp.economix.operaciones.model.enums.TipoTransaccion;
import pe.edu.pucp.economix.operaciones.model.Transaccion;
import pe.edu.pucp.economix.tesoreria.idao.IMonedaDAO;
import pe.edu.pucp.economix.tesoreria.idao.ICuentaBancariaDAO;
import pe.edu.pucp.economix.tesoreria.daoi.MonedaDAOImpl;
import pe.edu.pucp.economix.tesoreria.daoi.CuentaBancariaDAOImpl;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import pe.edu.pucp.economix.operaciones.daoi.SolicitudGastoDAOImpl;
import pe.edu.pucp.economix.operaciones.idao.ISolicitudGastoDAO;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoSolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoTransaccion;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;

import java.util.Date;
import java.util.List;

public class TransaccionBOImpl implements ITransaccionBO {

    private final ITransaccionDAO transaccionDAO;
    private final ICuentaBancariaDAO cbDAO;
    private final IMonedaDAO monedaDAO;
    public TransaccionBOImpl() {
        transaccionDAO= new TransaccionDAOImpl();
        cbDAO = new CuentaBancariaDAOImpl();
        monedaDAO= new MonedaDAOImpl();
    }


    private void validarIdUsuarioAccion(int idUsuarioAccion) throws Exception {
        if (idUsuarioAccion <= 0) {
            throw new Exception("El usuario de acción debe ser mayor que cero.");
        }
    }

    @Override
    public int insertar(Transaccion transaccion, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(transaccion,false);
        int resultado= transaccionDAO.insertar(transaccion, idUsuarioAccion);

        return resultado;
    }

    @Override
    public int modificar(Transaccion transaccion, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(transaccion,true);
        int result = transaccionDAO.modificar(transaccion, idUsuarioAccion);
        if (result > 0 
                && transaccion.getEstadoTransaccion() == EstadoTransaccion.COMPLETADA 
                && transaccion.getTipoTransaccion() == TipoTransaccion.DESEMBOLSO 
                && transaccion.getSolicitud() != null 
                && transaccion.getSolicitud().getIdSolicitudGasto() > 0) {
            ISolicitudGastoDAO solicitudDAO = new SolicitudGastoDAOImpl();
            SolicitudGasto solicitud = solicitudDAO.buscarPorId(transaccion.getSolicitud().getIdSolicitudGasto());
            if (solicitud != null) {
                solicitud.setEstado(EstadoSolicitudGasto.PAGADO);
                solicitudDAO.modificar(solicitud, idUsuarioAccion);
            }
        }
        return result;
    }

    @Override
    public int eliminar(int id, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        if (id <= 0) {
            throw new Exception("El id de la transaccion debe ser mayor que cero.");
        }
        return transaccionDAO.eliminar(id, idUsuarioAccion);
    }

    @Override
    public Transaccion buscarPorId(int id) throws Exception {
        return transaccionDAO.buscarPorId(id);
    }

    @Override
    public List<Transaccion> listarTodas() throws Exception {
        return transaccionDAO.listarTodas();
    }

    @Override
    public List<Transaccion> listarActivas() throws Exception {
        return transaccionDAO.listarActivas();
    }

    @Override
    public List<Transaccion> listarPorJefe(int idJefe) throws Exception {
        return transaccionDAO.listarPorJefe(idJefe);
    }

    @Override
    public List<Transaccion> listarPorEmpleado(int idEmpleado) throws Exception {
        return transaccionDAO.listarPorEmpleado(idEmpleado);
    }

    public void validar(Transaccion transaccion, boolean esModificacion) throws Exception{
        if(transaccion==null){
            throw new Exception("La transaccion no puede ser nula.");
        }
        if(esModificacion && transaccion.getIdTransaccion() <= 0){
            throw new Exception("El id de la transaccion es obligatorio para la modificación.");
        }

        // Siempre validar: tipo y monto
        validarTipo(transaccion.getTipoTransaccion());
        validarMontoTransaccion(transaccion.getMonto());

        // Solo en MODIFICACIÓN validar campos obligatorios completos
        if(esModificacion){
            validarMedioPagoYOperacion(transaccion);
            if (transaccion.getMedioPago() == MedioPago.TRANSFERENCIA) {
                validarCuentaOrigen(transaccion.getCuentaOrigen());
                validarCuentaDestino(transaccion.getCuentaDestino());
                validarMoneda(transaccion.getMoneda());
            }
            validarFecha(transaccion.getFecha());
        }
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
        if (transaccion.getMedioPago() == MedioPago.YAPE || transaccion.getMedioPago() == MedioPago.TRANSFERENCIA) {
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
        if(monto <= 0)
            throw new Exception("El monto de la transaccion debe ser mayor que cero.");
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
        if(moneda == null){
            throw new Exception("La moneda no puede ser nula.");
        }
        if(moneda.getIdMoneda() <= 0){
            throw new Exception("El id de la moneda debe ser mayor que cero.");
        }
        if(monedaDAO.buscarPorId(moneda.getIdMoneda())==null){
            throw new Exception("La moneda seleccionada no esta registrada.");
        }
    }

}
