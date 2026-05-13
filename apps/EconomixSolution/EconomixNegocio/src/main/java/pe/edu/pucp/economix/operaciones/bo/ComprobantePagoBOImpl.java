package pe.edu.pucp.economix.operaciones.bo;

import pe.edu.pucp.economix.operaciones.boi.IComprobantePagoBO;
import pe.edu.pucp.economix.operaciones.dao.IComprobantePagoDAO;
import pe.edu.pucp.economix.operaciones.implement.ComprobantePagoImplement;
import pe.edu.pucp.economix.operaciones.model.ComprobantePago;
import pe.edu.pucp.economix.operaciones.model.TipoComprobante;

import java.util.List;

public class ComprobantePagoBOImpl implements IComprobantePagoBO {
    private final IComprobantePagoDAO comprobantePagoDAO;

    public ComprobantePagoBOImpl(){
        comprobantePagoDAO= new ComprobantePagoImplement();
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

    }

    public void validarProveedor(ComprobantePago comprobante) throws Exception{
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
        if(comprobante.getNumeroSerial().isEmpty()){
            throw new Exception("Tiene que registrar el numero serial del documento.");
        }
        if(comprobante.getFechaEmision()==null){
            throw new Exception("Tiene que registrar la fecha en la que se genero el documento.");
        }
    }

}
