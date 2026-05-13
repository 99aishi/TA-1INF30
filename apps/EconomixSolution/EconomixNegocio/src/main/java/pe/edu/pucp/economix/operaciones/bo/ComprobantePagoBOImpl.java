package pe.edu.pucp.economix.operaciones.bo;

import pe.edu.pucp.economix.operaciones.boi.IComprobantePagoBO;
import pe.edu.pucp.economix.operaciones.boi.ISolicitudGastoBO;
import pe.edu.pucp.economix.operaciones.dao.IComprobantePagoDAO;
import pe.edu.pucp.economix.operaciones.implement.ComprobantePagoImplement;
import pe.edu.pucp.economix.operaciones.model.ComprobantePago;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.TipoComprobante;
import pe.edu.pucp.economix.tesoreria.bo.MonedaBOImpl;
import pe.edu.pucp.economix.tesoreria.boi.IMonedaBO;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.util.List;

public class ComprobantePagoBOImpl implements IComprobantePagoBO {
    private final IComprobantePagoDAO comprobantePagoDAO;
    private final IMonedaBO monedaBO;
    private final ISolicitudGastoBO solicitudGastoBO;
    public ComprobantePagoBOImpl(){
        comprobantePagoDAO= new ComprobantePagoImplement();
        monedaBO=new MonedaBOImpl();
        solicitudGastoBO=new SolicitudGastoBOImpl();
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

    public void validar(ComprobantePago comprobante, boolean EsModificacion) throws Exception{
        if(comprobante==null){
            throw new Exception("El comprobante no puede ser nulo.");
        }
        if(EsModificacion && comprobante.getIdComprobante()<=0){
            throw new Exception("El ID del Comprobante de Pago es necesario para la modificion");
        }

        validarDocumento(comprobante);
        validarProveedor(comprobante);
        validarNumeros(comprobante);
        validarMoneda(comprobante.getMoneda());
        validarSolicitud(comprobante.getSolicitud());
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

    public void validarNumeros(ComprobantePago comprobante) throws Exception {
        if(comprobante.getTotal()==0){
            throw new Exception("El total no puede ser cero.");
        }
        if(comprobante.getSubtotal()==0){
            throw new Exception("El subtotal no puede ser cero.");
        }
        if (comprobante.getIgv() == 0) {
            throw new Exception("El IGV no puede ser cero.");
        }
        if(comprobante.getTotal()!=comprobante.getSubtotal()+comprobante.getIgv()){
            throw new Exception("Los valores ingresados no suman correctamente.");
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

}
