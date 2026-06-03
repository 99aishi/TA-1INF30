package pe.edu.pucp.economix.operaciones.boi;

import pe.edu.pucp.economix.operaciones.ibo.ICicloCajaBO;
import pe.edu.pucp.economix.operaciones.ibo.IComprobantePagoBO;
import pe.edu.pucp.economix.operaciones.ibo.ISolicitudGastoBO;
import pe.edu.pucp.economix.operaciones.idao.IComprobantePagoDAO;
import pe.edu.pucp.economix.operaciones.daoi.ComprobantePagoDAOImpl;
import pe.edu.pucp.economix.operaciones.model.ComprobantePago;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.tesoreria.boi.MonedaBOImpl;
import pe.edu.pucp.economix.tesoreria.ibo.IMonedaBO;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.util.Date;
import java.util.List;

public class ComprobantePagoBOImpl implements IComprobantePagoBO {
    private final IComprobantePagoDAO comprobantePagoDAO;
    private final IMonedaBO monedaBO;
    private final ISolicitudGastoBO solicitudGastoBO;
    private final ICicloCajaBO cicloBO;
    public ComprobantePagoBOImpl(){
        comprobantePagoDAO= new ComprobantePagoDAOImpl();
        monedaBO=new MonedaBOImpl();
        solicitudGastoBO=new SolicitudGastoBOImpl();
        cicloBO= new CicloCajaBOImpl();
    }
    @Override
    public int insertar(ComprobantePago comprobante) throws Exception {
        validar(comprobante,false);
        return comprobantePagoDAO.insertar(comprobante);
    }

    @Override
    public int modificar(ComprobantePago comprobante) throws Exception {
        validar(comprobante,true);

        return comprobantePagoDAO.modificar(comprobante);
    }

    @Override
    public int eliminar(int id) throws Exception {
        return comprobantePagoDAO.eliminar(id);
    }

    @Override
    public ComprobantePago buscarPorId(int id) throws Exception {
        return comprobantePagoDAO.buscarPorId(id);
    }

    @Override
    public List<ComprobantePago> listarTodas() throws Exception {
        return comprobantePagoDAO.listarTodas();
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

        SolicitudGasto soli = solicitudGastoBO.buscarPorId(comprobante.getSolicitud().getIdSolicitudGasto());
        boolean esGastoExcepcional = soli.getMontoSolicitado() <= 50;

        // 2. Validación de números adaptada
        validarNumeros(comprobante, esGastoExcepcional);

        // 3. Validaciones estrictamente fiscales
        if(!esGastoExcepcional){
            validarDocumento(comprobante);
            validarProveedor(comprobante);
        }

    }


    public void validarSolicitud(SolicitudGasto solicitud) throws Exception {
        if(solicitud==null){
            throw new Exception("El comprobante tiene que tener una solicitud asociada.");
        }

        if(solicitudGastoBO.buscarPorId(solicitud.getIdSolicitudGasto())==null){
            throw new Exception("La solicitud asociada no existe.");
        }
    }

    public void validarMoneda(Moneda moneda) throws Exception {
        if(moneda==null){
            throw new Exception("El comprobante tiene que tener una moneda asignada.");
        }
        if(monedaBO.buscarPorId(moneda.getIdMoneda())==null){
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
    public void validarFecha(ComprobantePago comprobante) throws Exception{
        Date fechaEmision=comprobante.getFechaEmision();
        if(fechaEmision == null) {
            throw new Exception("La fecha de emisión es obligatoria.");
        }
        SolicitudGasto soli = solicitudGastoBO.buscarPorId(comprobante.getSolicitud().getIdSolicitudGasto());
        Date fechaSolicitado= soli.getFechaSolicitud();
        Date fechaActual= new Date();
        if(fechaEmision.after(fechaActual)){
            throw new Exception("La fecha del comprobante no puede ser posterior a la fecha actual.");
        }
        if(fechaEmision.before(fechaSolicitado)){
            throw new Exception("La fecha del comprobante no puede ser antes de la fecha de la solicitud relacionada.");
        }
        Date inicioCiclo = cicloBO.buscarPorId(soli.getCiclo().getIdCicloCaja()).getFechaApertura();
        Date finCiclo = cicloBO.buscarPorId(soli.getCiclo().getIdCicloCaja()).getFechaCierre();

        if (fechaEmision.before(inicioCiclo) || (finCiclo != null && fechaEmision.after(finCiclo))) {
            throw new Exception("La fecha del comprobante no pertenece al ciclo activo de la caja chica.");
        }
    }

}
