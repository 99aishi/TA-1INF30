package pe.edu.pucp.economix.operaciones.boi;

import pe.edu.pucp.economix.operaciones.ibo.IComprobantePagoBO;
import pe.edu.pucp.economix.operaciones.idao.ICicloCajaChicaDAO;
import pe.edu.pucp.economix.operaciones.idao.IComprobantePagoDAO;
import pe.edu.pucp.economix.operaciones.idao.ISolicitudGastoDAO;
import pe.edu.pucp.economix.operaciones.daoi.CicloCajaChicaDAOImpl;
import pe.edu.pucp.economix.operaciones.daoi.ComprobantePagoDAOImpl;
import pe.edu.pucp.economix.operaciones.daoi.SolicitudGastoDAOImpl;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.ComprobantePago;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoComprobante;
import pe.edu.pucp.economix.tesoreria.idao.IMonedaDAO;
import pe.edu.pucp.economix.tesoreria.daoi.MonedaDAOImpl;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.util.Date;
import java.util.List;

import pe.edu.pucp.economix.config.DBManager;

import pe.edu.pucp.economix.operaciones.idao.ITransaccionDAO;
import pe.edu.pucp.economix.operaciones.daoi.TransaccionDAOImpl;
import pe.edu.pucp.economix.operaciones.model.Transaccion;
import pe.edu.pucp.economix.operaciones.model.enums.TipoTransaccion;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoTransaccion;
import pe.edu.pucp.economix.tesoreria.idao.ICajaChicaDAO;
import pe.edu.pucp.economix.tesoreria.daoi.CajaChicaDAOImpl;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;

public class ComprobantePagoBOImpl implements IComprobantePagoBO {
    private final IComprobantePagoDAO comprobantePagoDAO;
    private final IMonedaDAO monedaDAO;
    private final ISolicitudGastoDAO solicitudGastoDAO;
    private final ICicloCajaChicaDAO cicloCajaChicaDAO;
    public ComprobantePagoBOImpl(){
        comprobantePagoDAO= new ComprobantePagoDAOImpl();
        monedaDAO=new MonedaDAOImpl();
        solicitudGastoDAO=new SolicitudGastoDAOImpl();
        cicloCajaChicaDAO= new CicloCajaChicaDAOImpl();
    }
    private void validarIdUsuarioAccion(int idUsuarioAccion) throws Exception {
        if (idUsuarioAccion <= 0) {
            throw new Exception("El usuario de acción debe ser mayor que cero.");
        }
    }

    @Override
    public int insertar(ComprobantePago comprobante, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(comprobante,false);
        DBManager.getDBManager().iniciarTransaccion();
        try {
            int id = comprobantePagoDAO.insertar(comprobante, idUsuarioAccion);
            actualizarDevolucionSobrante(comprobante.getSolicitud().getIdSolicitudGasto(), idUsuarioAccion);
            DBManager.getDBManager().confirmarTransaccion();
            return id;
        } catch (Exception ex) {
            try {
                DBManager.getDBManager().cancelarTransaccion();
            } catch (Exception rollbackEx) {
                System.err.println("Error al hacer rollback en insertar: " + rollbackEx.getMessage());
            }
            throw ex;
        }
    }

    @Override
    public int modificar(ComprobantePago comprobante, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(comprobante,true);
        DBManager.getDBManager().iniciarTransaccion();
        try {
            int r = comprobantePagoDAO.modificar(comprobante, idUsuarioAccion);
            actualizarDevolucionSobrante(comprobante.getSolicitud().getIdSolicitudGasto(), idUsuarioAccion);
            DBManager.getDBManager().confirmarTransaccion();
            return r;
        } catch (Exception ex) {
            try {
                DBManager.getDBManager().cancelarTransaccion();
            } catch (Exception rollbackEx) {
                System.err.println("Error al hacer rollback en modificar: " + rollbackEx.getMessage());
            }
            throw ex;
        }
    }

    @Override
    public int eliminar(int id, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        DBManager.getDBManager().iniciarTransaccion();
        try {
            ComprobantePago comprobante = comprobantePagoDAO.buscarPorId(id);
            int r = comprobantePagoDAO.eliminar(id, idUsuarioAccion);
            if (comprobante != null && comprobante.getSolicitud() != null) {
                actualizarDevolucionSobrante(comprobante.getSolicitud().getIdSolicitudGasto(), idUsuarioAccion);
            }
            DBManager.getDBManager().confirmarTransaccion();
            return r;
        } catch (Exception ex) {
            try {
                DBManager.getDBManager().cancelarTransaccion();
            } catch (Exception rollbackEx) {
                System.err.println("Error al hacer rollback en eliminar: " + rollbackEx.getMessage());
            }
            throw ex;
        }
    }

    @Override
    public ComprobantePago buscarPorId(int id) throws Exception {
        return comprobantePagoDAO.buscarPorId(id);
    }

    @Override
    public List<ComprobantePago> listarTodas() throws Exception {
        return comprobantePagoDAO.listarTodas();
    }

    @Override
    public List<ComprobantePago> listarActivas() throws Exception {
        return comprobantePagoDAO.listarActivas();
    }

    public List<ComprobantePago> listarPorSolicitud(int idSolicitud)throws Exception {
        return comprobantePagoDAO.listarPorSolicitud( idSolicitud);
    }

    public void validar(ComprobantePago comprobante, boolean EsModificacion) throws Exception{
        if(comprobante==null){
            throw new Exception("El comprobante no puede ser nulo.");
        }
        if(EsModificacion && comprobante.getIdComprobante()<=0){
            throw new Exception("El ID del Comprobante de Pago es necesario para la modificion");
        }
        validarSolicitud(comprobante.getSolicitud());
        validarMoneda(comprobante.getMoneda());
        validarFecha(comprobante);
        validarContraSolicitud(comprobante, EsModificacion);

        SolicitudGasto soli = solicitudGastoDAO.buscarPorId(comprobante.getSolicitud().getIdSolicitudGasto());
        boolean esGastoExcepcional = soli.getMontoSolicitado() <= 50;

        // 2. Validación de números adaptada
        validarNumeros(comprobante, esGastoExcepcional);

        // 3. Validaciones estrictamente fiscales
        if(!esGastoExcepcional){
            validarDocumento(comprobante);
            validarProveedor(comprobante);
        }

    }

    public void validarContraSolicitud(ComprobantePago comprobante, boolean esModificacion) throws Exception {
        SolicitudGasto soli = solicitudGastoDAO.buscarPorId(comprobante.getSolicitud().getIdSolicitudGasto());
        List<ComprobantePago> comprobantesExistentes = comprobantePagoDAO.listarPorSolicitud(soli.getIdSolicitudGasto());

        double montoComprobantes = 0;
        for (ComprobantePago c : comprobantesExistentes) {
            if (c.getEstado() != null && c.getEstado().toString().equals("ANULADO")) {
                continue;
            }
            if (esModificacion && c.getIdComprobante() == comprobante.getIdComprobante()) {
                continue;
            }
            montoComprobantes += c.getTotal();
        }

        double montoDisponible = soli.getMontoSolicitado() - montoComprobantes;
        if (comprobante.getTotal() > montoDisponible) {
            throw new Exception("El total del comprobante (" + String.format("%.2f", comprobante.getTotal())
                    + ") excede el monto disponible de la solicitud (" + String.format("%.2f", montoDisponible) + ").");
        }
    }


    public void validarSolicitud(SolicitudGasto solicitud) throws Exception {
        if(solicitud==null){
            throw new Exception("El comprobante tiene que tener una solicitud asociada.");
        }

        if(solicitudGastoDAO.buscarPorId(solicitud.getIdSolicitudGasto())==null){
            throw new Exception("La solicitud asociada no existe.");
        }
    }

    public void validarMoneda(Moneda moneda) throws Exception {
        if(moneda==null){
            throw new Exception("El comprobante tiene que tener una moneda asignada.");
        }
        if(monedaDAO.buscarPorId(moneda.getIdMoneda())==null){
            throw new Exception("La moneda no esta registrada.");
        }
    }

    public void validarNumeros(ComprobantePago comprobante, boolean esGastoExcepcional) throws Exception {
        if(comprobante.getTotal() <= 0){
            throw new Exception("El total debe ser mayor a cero.");
        }
        if(!esGastoExcepcional) {
            if(comprobante.getSubtotal() <= 0){
                throw new Exception("El subtotal no puede ser cero en un comprobante fiscal.");
            }
            if (comprobante.getIgv() < 0) { // El IGV podría ser 0 en boletas, pero jamás negativo
                throw new Exception("El IGV no puede ser negativo.");
            }
            if(Math.abs(comprobante.getTotal() - (comprobante.getSubtotal() + comprobante.getIgv())) > 0.01){
                throw new Exception("Los valores ingresados no suman correctamente.");
            }
        }
    }

    public void validarProveedor(ComprobantePago comprobante) throws Exception{

        if(comprobante.getRUCProveedor()==null){
            throw new Exception("Debe registrar el RUC del Proveedor.");
        }
        if(comprobante.getRUCProveedor().isEmpty()){
            throw new Exception("Debe registrar el RUC del Proveedor.");
        }
        if(comprobante.getRazonSocial().isEmpty()){
            throw new Exception("Debe registrar la Razon Social del Proveedor.");
        }
    }

    public void validarDocumento(ComprobantePago comprobante) throws Exception{
        if(comprobante.getTipoDocumento()==null){
            throw new Exception("Tiene que escoger un tipo de documento.");
        }
        if(comprobante.getNumeroSerial()==null){
            throw new Exception("Tiene que indicar el numero serial del documento.");
        }
        if(comprobante.getNumeroSerial().isEmpty()){
            throw new Exception("Tiene que registrar el numero serial del documento.");
        }
        if(comprobante.getFechaEmision()==null){
            throw new Exception("Tiene que registrar la fecha en la que se genero el documento.");
        }
    }
    private Date truncarFecha(Date fecha) {
        if (fecha == null) return null;
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(fecha);
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public void validarFecha(ComprobantePago comprobante) throws Exception{
        Date fechaEmision = comprobante.getFechaEmision();
        if(fechaEmision == null) {
            throw new Exception("La fecha de emisión es obligatoria.");
        }
        
        Date fechaEmisionTruncada = truncarFecha(fechaEmision);
        SolicitudGasto soli = solicitudGastoDAO.buscarPorId(comprobante.getSolicitud().getIdSolicitudGasto());
        Date fechaSolicitadoTruncada = truncarFecha(soli.getFechaSolicitud());
        Date fechaActualTruncada = truncarFecha(new Date());

        if(fechaEmisionTruncada.after(fechaActualTruncada)){
            throw new Exception("La fecha del comprobante no puede ser posterior a la fecha actual.");
        }
        if(fechaEmisionTruncada.before(fechaSolicitadoTruncada)){
            throw new Exception("La fecha del comprobante no puede ser antes de la fecha de la solicitud relacionada.");
        }
        CicloCajaChica ciclo = cicloCajaChicaDAO.buscarPorId(soli.getCiclo().getIdCicloCaja());
        Date inicioCicloTruncado = truncarFecha(ciclo.getFechaApertura());
        Date finCicloTruncado = truncarFecha(ciclo.getFechaCierre());

        if (fechaEmisionTruncada.before(inicioCicloTruncado) || (finCicloTruncado != null && fechaEmisionTruncada.after(finCicloTruncado))) {
            throw new Exception("La fecha del comprobante no pertenece al ciclo activo de la caja chica.");
        }
    }

    @Override
    public int evaluar(int idComprobante, boolean aprobar, String observacion, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        if (idComprobante <= 0) throw new Exception("El id del comprobante debe ser mayor que cero.");

        ComprobantePago comprobante = comprobantePagoDAO.buscarPorId(idComprobante);
        if (comprobante == null) throw new Exception("El comprobante de pago no existe.");

        EstadoComprobante estadoActual = comprobante.getEstado();
        if (estadoActual == EstadoComprobante.ANULADO) {
            throw new Exception("No se puede evaluar un comprobante anulado.");
        }

        if (aprobar) {
            if (estadoActual != EstadoComprobante.POR_REVISAR && estadoActual != EstadoComprobante.OBSERVADO) {
                throw new Exception("Solo se puede aprobar un comprobante POR_REVISAR u OBSERVADO.");
            }
            comprobante.setEstado(EstadoComprobante.APROBADO);
        } else {
            if (estadoActual == EstadoComprobante.APROBADO) {
                throw new Exception("No se puede observar un comprobante ya aprobado.");
            }
            comprobante.setEstado(EstadoComprobante.OBSERVADO);
        }

        // La observación se almacena en el nombre del archivo comprobante como comentario temporal
        // o se ignora si no hay campo. Se deja para extensión futura.
        return comprobantePagoDAO.modificar(comprobante, idUsuarioAccion);
    }

    private void actualizarDevolucionSobrante(int idSolicitudGasto, int idUsuarioAccion) throws Exception {
        SolicitudGasto soli = solicitudGastoDAO.buscarPorId(idSolicitudGasto);
        if (soli == null) return;

        List<ComprobantePago> comprobantes = comprobantePagoDAO.listarPorSolicitud(idSolicitudGasto);
        double sumaComprobantes = 0;
        int countComprobantes = 0;
        for (ComprobantePago cp : comprobantes) {
            if (cp.getEstado() != EstadoComprobante.ANULADO) {
                sumaComprobantes += cp.getTotal();
                countComprobantes++;
            }
        }

        ITransaccionDAO transaccionDAO = new TransaccionDAOImpl();
        List<Transaccion> transacciones = transaccionDAO.listarTodas();
        Transaccion devolucion = null;
        for (Transaccion t : transacciones) {
            if (t.getIdSolicitudGasto() == idSolicitudGasto 
                && t.getTipoTransaccion() == TipoTransaccion.DEVOLUCION_SOBRANTE
                && t.getEstadoTransaccion() != EstadoTransaccion.ANULADA) {
                devolucion = t;
                break;
            }
        }

        double montoSolicitado = soli.getMontoSolicitado();
        double sobrante = montoSolicitado - sumaComprobantes;

        if (countComprobantes > 0 && sobrante > 0) {
            if (devolucion == null) {
                devolucion = new Transaccion();
                devolucion.setTipoTransaccion(TipoTransaccion.DEVOLUCION_SOBRANTE);
                devolucion.setMonto(sobrante);
                devolucion.setBeneficiario(soli.getSolicitante());
                devolucion.setEstadoTransaccion(EstadoTransaccion.REGISTRADA);
                devolucion.setIdSolicitudGasto(idSolicitudGasto);
                devolucion.setMoneda(soli.getMonedaOriginal());
                devolucion.setFecha(new Date());
                
                if (soli.getCiclo() != null && soli.getCiclo().getCajaChica() != null) {
                    int idCajaChica = soli.getCiclo().getCajaChica().getIdFondo();
                    ICajaChicaDAO cajaChicaDAO = new CajaChicaDAOImpl();
                    CajaChica ccCompleta = cajaChicaDAO.buscarPorId(idCajaChica);
                    if (ccCompleta != null) {
                        devolucion.setCuentaDestino(ccCompleta.getCuentaBancaria());
                    }
                }
                
                transaccionDAO.insertar(devolucion, idUsuarioAccion);
            } else {
                devolucion.setMonto(sobrante);
                devolucion.setEstadoTransaccion(EstadoTransaccion.REGISTRADA);
                transaccionDAO.modificar(devolucion, idUsuarioAccion);
            }
        } else {
            if (devolucion != null) {
                transaccionDAO.eliminar(devolucion.getIdTransaccion(), idUsuarioAccion);
            }
        }
    }

}
