package pe.edu.pucp.economix.operaciones.bo;

import pe.edu.pucp.economix.operaciones.boi.ICicloCajaBO;
import pe.edu.pucp.economix.operaciones.boi.ISolicitudGastoBO;
import pe.edu.pucp.economix.operaciones.dao.ISolicitudGastoDAO;
import pe.edu.pucp.economix.operaciones.implement.SolicitudGastoImplement;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.rrhh.model.Empleado;

import java.util.Date;
import java.util.List;

public class SolicitudGastoBOImpl implements ISolicitudGastoBO {

    private final ISolicitudGastoDAO solicitudGastoDAO;
    private final ICicloCajaBO cicloCajaBO ;
    public SolicitudGastoBOImpl(){
        solicitudGastoDAO= new SolicitudGastoImplement();
        cicloCajaBO =  new CicloCajaBOImpl();
    }
    @Override
    public int insertar(SolicitudGasto solicitud) throws Exception {
        validar(solicitud,false);
        return solicitudGastoDAO.insertar(solicitud);
    }

    @Override
    public int modificar(SolicitudGasto solicitud) throws Exception {
        validar(solicitud,true);
        return solicitudGastoDAO.modificar(solicitud);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if(id <=0 ){
            throw new Exception("Debe ingresar un ID de Solicitud de Gasto valido.");
        }
        return solicitudGastoDAO.eliminar(id);
    }

    @Override
    public SolicitudGasto buscarPorId(int id) throws Exception {
        if(id <=0 ){
            throw new Exception("Debe ingresar un ID de Solicitud de Gasto valido.");
        }
        return solicitudGastoDAO.buscarPorId(id);
    }

    @Override
    public List<SolicitudGasto> listarTodas() throws Exception {
        return solicitudGastoDAO.listarTodas();
    }

    public void validar(SolicitudGasto solicitudGasto, boolean EsModificacion) throws Exception{
        if(solicitudGasto==null){
            throw new Exception("La Solicitud de Gasto no debe ser nula.");
        }
        if(EsModificacion && solicitudGasto.getIdSolicitudGasto()<=0){
            throw new Exception("Debe ingresar un ID de Solicitud de Gasto valido.");
        }

        validarFecha(solicitudGasto.getFechaSolicitud());
        validarSolicitante(solicitudGasto.getSolicitante());
        validarDestinatario(solicitudGasto.getDestinatario());
        validarMotivo(solicitudGasto.getMotivoSolicitud());
        validarCiclo(solicitudGasto.getCiclo());
        validarMonto(solicitudGasto);
    }
    public void validarFecha(Date fecha) throws Exception{
        if(fecha==null){
            throw new Exception("La fecha no puede ser nula.");
        }
    }
    public void validarSolicitante(Empleado solicitante) throws Exception{
        if(solicitante==null){
            throw new Exception("El solicitante no puede ser nulo.");
        }
    }
    public void validarDestinatario(Empleado destinatario) throws Exception{
        if(destinatario==null){
            throw new Exception("El destinatario no puede ser nulo.");
        }
    }
    public void validarCiclo(CicloCajaChica ciclo) throws Exception{
        if(ciclo==null){
            throw new Exception("El ciclo no puede ser nulo.");
        }
        if(cicloCajaBO.buscarPorId(ciclo.getIdCicloCaja())==null){
            throw new Exception("El ciclo no existe.");
        }
    }
    public void validarMotivo(String motivo)throws Exception{
        if(motivo.length() > 200){
            throw new Exception("El motivo excede los caracteres maximos.");
        }
        if(motivo.isEmpty()){
            throw new Exception("Debe ingresar el motivo de la solicitud.");
        }
    }

    public void validarMonto(SolicitudGasto solicitudGasto)throws Exception{
        if(solicitudGasto.getMontoSolicitado()==0){
            throw new Exception("El monto solicitado no puede ser cero.");
        }

        if(solicitudGasto.getMontoSolicitado()>solicitudGasto.getCiclo().getSaldoInicial()-solicitudGasto.getCiclo().getTotalGastado()){
            throw new Exception("El monto solicitado no se puede otorgar. Fondos Insuficientes.");
        }
    }


}
