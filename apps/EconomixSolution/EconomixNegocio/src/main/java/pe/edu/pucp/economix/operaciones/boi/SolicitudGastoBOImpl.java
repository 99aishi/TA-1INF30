package pe.edu.pucp.economix.operaciones.boi;

import pe.edu.pucp.economix.operaciones.ibo.ICicloCajaBO;
import pe.edu.pucp.economix.operaciones.ibo.IComprobantePagoBO;
import pe.edu.pucp.economix.operaciones.ibo.ISolicitudGastoBO;
import pe.edu.pucp.economix.operaciones.idao.IComprobantePagoDAO;
import pe.edu.pucp.economix.operaciones.idao.ISolicitudGastoDAO;
import pe.edu.pucp.economix.operaciones.daoi.SolicitudGastoDAOImpl;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.ComprobantePago;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoSolicitudGasto;
import pe.edu.pucp.economix.rrhh.model.Empleado;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SolicitudGastoBOImpl implements ISolicitudGastoBO {

    private final ISolicitudGastoDAO solicitudGastoDAO;
    private final ICicloCajaBO cicloCajaBO ;
    private final IComprobantePagoBO comprobantePagoBO;
    public SolicitudGastoBOImpl(){
        solicitudGastoDAO= new SolicitudGastoDAOImpl();
        cicloCajaBO =  new CicloCajaBOImpl();
        comprobantePagoBO=new ComprobantePagoBOImpl();
    }
    private void validarIdUsuarioAccion(int idUsuarioAccion) throws Exception {
        if (idUsuarioAccion <= 0) {
            throw new Exception("El usuario de acción debe ser mayor que cero.");
        }
    }

    @Override
    public int insertar(SolicitudGasto solicitud, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(solicitud,false);
        return solicitudGastoDAO.insertar(solicitud, idUsuarioAccion);
    }

    @Override
    public int modificar(SolicitudGasto solicitud, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(solicitud,true);
        return solicitudGastoDAO.modificar(solicitud, idUsuarioAccion);
    }

    @Override
    public int eliminar(int id, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        if(id <=0 ){
            throw new Exception("Debe ingresar un ID de Solicitud de Gasto valido.");
        }
        return solicitudGastoDAO.eliminar(id, idUsuarioAccion);
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

    @Override
    public List<SolicitudGasto> listarActivas() throws Exception {
        return solicitudGastoDAO.listarActivas();
    }

    public List<SolicitudGasto> listarPorSolicitante(int idSolicitante) throws Exception {
        return solicitudGastoDAO.listarPorSolicitante(idSolicitante);
    }

    public List<SolicitudGasto> listarPendientesJefe(int idUsuarioDestinatario) throws Exception {
        return solicitudGastoDAO.listarPendientesJefe(idUsuarioDestinatario);
    }

    public List<SolicitudGasto> listarPorCiclo(int idCicloCaja) throws Exception {
        return solicitudGastoDAO.listarPorCiclo(idCicloCaja);
    }

    public void validar(SolicitudGasto solicitudGasto, boolean EsModificacion) throws Exception{
        if(solicitudGasto==null){
            throw new Exception("La Solicitud de Gasto no debe ser nula.");
        }
        if(EsModificacion && solicitudGasto.getIdSolicitudGasto()<=0){
            throw new Exception("Debe ingresar un ID de Solicitud de Gasto valido.");
        }
        if(EsModificacion){
            validarCambioEstado(solicitudGasto);
        }
        validarFecha(solicitudGasto);
        validarInvolucrados(solicitudGasto);
        validarMotivo(solicitudGasto.getMotivoSolicitud());
        validarCiclo(solicitudGasto.getCiclo());
        validarMonto(solicitudGasto);
    }

    public void validarFecha(SolicitudGasto solicitudGasto) throws Exception{
        Date fecha = solicitudGasto.getFechaSolicitud();
        if(fecha==null){
            throw new Exception("La fecha no puede ser nula.");
        }
        CicloCajaChica ciclo =cicloCajaBO.buscarPorId(solicitudGasto.getCiclo().getIdCicloCaja());
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        int diaDeLaSemana = cal.get(Calendar.DAY_OF_WEEK);
        if(diaDeLaSemana == Calendar.SATURDAY || diaDeLaSemana == Calendar.SUNDAY){
            throw new Exception("El ciclo semanal se encuentra cerrado.");
        }

        if(fecha.before(ciclo.getFechaApertura()) || fecha.after(ciclo.getFechaCierre())){
            throw new Exception("La fecha de la solicitud no esta dentro del Ciclo Caja Chica indicado");
        }
    }

    public void validarInvolucrados(SolicitudGasto solicitudGasto) throws Exception{
        if(solicitudGasto.getSolicitante()==null){
            throw new Exception("El solicitante no puede ser nulo.");
        }
        if(solicitudGasto.getDestinatario()==null){
            throw new Exception("El destinatario no puede ser nulo.");
        }
        if(solicitudGasto.getSolicitante().getUsuarioID()==solicitudGasto.getDestinatario().getUsuarioID()){
            throw new Exception("El destinatario no puede ser el solicitante.");
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
        if(solicitudGasto.getMontoSolicitado() <= 0){
            throw new Exception("El monto solicitado debe ser mayor que cero.");
        }

        double saldoDisponible = solicitudGasto.getCiclo().getSaldoInicial()
                - solicitudGasto.getCiclo().getTotalGastado();
        if(saldoDisponible < 0){
            throw new Exception("El ciclo no tiene saldo disponible.");
        }
        if(solicitudGasto.getMontoSolicitado() > saldoDisponible){
            throw new Exception("El monto solicitado no se puede otorgar. Fondos Insuficientes.");
        }

        double limiteMaximo = saldoDisponible * 0.50;
        if(solicitudGasto.getMontoSolicitado() > limiteMaximo){
            throw new Exception("El monto solicitado supera el limite del 50% del saldo disponible ("
                    + String.format("%.2f", limiteMaximo) + ").");
        }

        verificarLimiteAcumulado(solicitudGasto, saldoDisponible);
    }

    private void verificarLimiteAcumulado(SolicitudGasto solicitudGasto,
                                          double saldoDisponible) throws Exception {
        if(solicitudGasto.getSolicitante() == null) return;

        List<SolicitudGasto> solicitudesDelEmpleado =
                solicitudGastoDAO.listarPorSolicitante(
                        solicitudGasto.getSolicitante().getUsuarioID());

        Date hace24Horas = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        double acumulado = 0;

        for(SolicitudGasto s : solicitudesDelEmpleado){
            if(s.getEstado() == EstadoSolicitudGasto.PENDIENTE
                    || s.getEstado() == EstadoSolicitudGasto.APROBADO){
                if(s.getFechaSolicitud() != null
                        && s.getFechaSolicitud().after(hace24Horas)){
                    acumulado += s.getMontoSolicitado();
                }
            }
        }

        double totalAcumulado = acumulado + solicitudGasto.getMontoSolicitado();
        double limiteMaximo = saldoDisponible * 0.50;

        if(totalAcumulado > limiteMaximo){
            throw new Exception("La suma acumulada de solicitudes PENDIENTE/APROBADO "
                    + "en las ultimas 24 horas (S/ " + String.format("%.2f", totalAcumulado)
                    + ") supera el 50% del saldo disponible.");
        }
    }

    public void validarCambioEstado(SolicitudGasto solicitudGasto)throws Exception{
        SolicitudGasto soliAModificar =solicitudGastoDAO.buscarPorId(solicitudGasto.getIdSolicitudGasto());
        if(soliAModificar.getEstado() == EstadoSolicitudGasto.APROBADO ||soliAModificar.getEstado() == EstadoSolicitudGasto.RECHAZADO){
            throw new Exception("No se puede modificar una solicitud Aprobada o Rechazada");
        }

        if(solicitudGasto.getEstado() == EstadoSolicitudGasto.RECHAZADO ){
            if(solicitudGasto.getComentarioDecision()==null){
                throw new Exception("Necesita ingresar un comentario justificando el rechazo.");
            }
        }
    }

}
