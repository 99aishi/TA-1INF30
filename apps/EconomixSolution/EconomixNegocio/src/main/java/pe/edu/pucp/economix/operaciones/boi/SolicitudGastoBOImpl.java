package pe.edu.pucp.economix.operaciones.boi;

import pe.edu.pucp.economix.operaciones.ibo.ISolicitudGastoBO;
import pe.edu.pucp.economix.operaciones.idao.ICicloCajaChicaDAO;
import pe.edu.pucp.economix.operaciones.idao.ISolicitudGastoDAO;
import pe.edu.pucp.economix.operaciones.idao.ITransaccionDAO;
import pe.edu.pucp.economix.operaciones.daoi.CicloCajaChicaDAOImpl;
import pe.edu.pucp.economix.operaciones.daoi.SolicitudGastoDAOImpl;
import pe.edu.pucp.economix.operaciones.daoi.TransaccionDAOImpl;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.Transaccion;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoSolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoTransaccion;
import pe.edu.pucp.economix.operaciones.model.enums.MedioPago;
import pe.edu.pucp.economix.operaciones.model.enums.TipoTransaccion;
import pe.edu.pucp.economix.rrhh.idao.IEmpleadoDAO;
import pe.edu.pucp.economix.rrhh.daoi.EmpleadoDAOImpl;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.tesoreria.idao.ICuentaBancariaDAO;
import pe.edu.pucp.economix.tesoreria.idao.ITipoCambioDAO;
import pe.edu.pucp.economix.tesoreria.daoi.CuentaBancariaDAOImpl;
import pe.edu.pucp.economix.tesoreria.daoi.TipoCambioDAOImpl;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;
import pe.edu.pucp.economix.tesoreria.model.Moneda;
import pe.edu.pucp.economix.tesoreria.model.TipoCambio;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SolicitudGastoBOImpl implements ISolicitudGastoBO {

    private final ISolicitudGastoDAO solicitudGastoDAO;
    private final ICicloCajaChicaDAO cicloCajaChicaDAO;
    private final ITransaccionDAO transaccionDAO;
    private final ICuentaBancariaDAO cuentaBancariaDAO;
    private final IEmpleadoDAO empleadoDAO;
    private final ITipoCambioDAO tipoCambioDAO;
    public SolicitudGastoBOImpl(){
        solicitudGastoDAO= new SolicitudGastoDAOImpl();
        cicloCajaChicaDAO =  new CicloCajaChicaDAOImpl();
        transaccionDAO=new TransaccionDAOImpl();
        cuentaBancariaDAO=new CuentaBancariaDAOImpl();
        empleadoDAO=new EmpleadoDAOImpl();
        tipoCambioDAO = new TipoCambioDAOImpl();
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
        calcularTipoCambioYMontoConvertido(solicitud);
        return solicitudGastoDAO.insertar(solicitud, idUsuarioAccion);
    }

    private void calcularTipoCambioYMontoConvertido(SolicitudGasto solicitud) throws Exception {
        if (solicitud.getMonedaOriginal() == null || solicitud.getCiclo() == null) {
            solicitud.setTipoCambio(1.0);
            solicitud.setMontoConvertido(solicitud.getMontoSolicitado());
            return;
        }

        Moneda monedaSolicitud = solicitud.getMonedaOriginal();
        CicloCajaChica cicloCompleto = cicloCajaChicaDAO.buscarPorId(solicitud.getCiclo().getIdCicloCaja());
        if (cicloCompleto == null || cicloCompleto.getCajaChica() == null || cicloCompleto.getCajaChica().getMoneda() == null) {
            throw new Exception("No se pudo determinar la moneda de la caja chica del ciclo.");
        }
        Moneda monedaCajaChica = cicloCompleto.getCajaChica().getMoneda();

        if (monedaSolicitud.getIdMoneda() == monedaCajaChica.getIdMoneda()) {
            solicitud.setTipoCambio(1.0);
            solicitud.setMontoConvertido(solicitud.getMontoSolicitado());
            return;
        }

        Date fecha = solicitud.getFechaSolicitud() != null ? solicitud.getFechaSolicitud() : new Date();
        TipoCambio tipoCambio = tipoCambioDAO.buscarPorMonedasYFecha(
                monedaSolicitud.getIdMoneda(), monedaCajaChica.getIdMoneda(), new java.sql.Date(fecha.getTime()));

        if (tipoCambio == null) {
            throw new Exception("No existe tipo de cambio registrado entre la moneda solicitada y la moneda de la caja chica para la fecha indicada.");
        }

        double factor = tipoCambio.getValor();
        solicitud.setTipoCambio(factor);
        solicitud.setMontoConvertido(solicitud.getMontoSolicitado() * factor);
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
        CicloCajaChica ciclo =cicloCajaChicaDAO.buscarPorId(solicitudGasto.getCiclo().getIdCicloCaja());
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        int diaDeLaSemana = cal.get(Calendar.DAY_OF_WEEK);
        if(diaDeLaSemana == Calendar.SATURDAY || diaDeLaSemana == Calendar.SUNDAY){
            throw new Exception("El ciclo semanal se encuentra cerrado.");
        }

        if(fecha.before(ciclo.getFechaApertura()) || (ciclo.getFechaCierre() != null && fecha.after(ciclo.getFechaCierre()))){
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
        if(cicloCajaChicaDAO.buscarPorId(ciclo.getIdCicloCaja())==null){
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

        double acumulado = 0;
        if(solicitudesDelEmpleado != null){
            Date hace24Horas = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);

            for(SolicitudGasto s : solicitudesDelEmpleado){
                if(s.getEstado() == EstadoSolicitudGasto.PENDIENTE
                        || s.getEstado() == EstadoSolicitudGasto.APROBADO){
                    if(s.getFechaSolicitud() != null
                            && s.getFechaSolicitud().after(hace24Horas)){
                        acumulado += s.getMontoSolicitado();
                    }
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

    @Override
    public int evaluar(int idSolicitudGasto, boolean aprobado, String comentario, int idJefeEvaluador, String numeroOperacionBancaria, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        if (idSolicitudGasto <= 0) throw new Exception("El id de la solicitud debe ser mayor que cero.");
        if (idJefeEvaluador <= 0) throw new Exception("El id del jefe evaluador debe ser mayor que cero.");

        SolicitudGasto solicitud = solicitudGastoDAO.buscarPorId(idSolicitudGasto);
        if (solicitud == null) throw new Exception("La solicitud de gasto no existe.");
        if (solicitud.getEstado() != EstadoSolicitudGasto.PENDIENTE) {
            throw new Exception("Solo se puede evaluar una solicitud en estado PENDIENTE.");
        }

        Empleado jefe = empleadoDAO.buscarPorId(idJefeEvaluador);
        if (jefe == null) throw new Exception("El jefe evaluador no existe.");

        if (aprobado) {
            if (numeroOperacionBancaria == null || numeroOperacionBancaria.trim().isEmpty()) {
                throw new Exception("Debe ingresar el número de operación bancaria para aprobar el desembolso.");
            }
            solicitud.setEstado(EstadoSolicitudGasto.APROBADO);
            solicitud.setJefeAprobador(jefe);
            solicitud.setComentarioDecision(comentario);

            int idModificado = solicitudGastoDAO.modificar(solicitud, idUsuarioAccion);

            // Generar transacción de desembolso
            generarTransaccionDesembolso(solicitud, numeroOperacionBancaria, idUsuarioAccion);

            // Actualizar total gastado del ciclo
            if (solicitud.getCiclo() != null) {
                calcularTotalGastado(solicitud.getCiclo(), idUsuarioAccion);
            }

            return idModificado;
        } else {
            if (comentario == null || comentario.trim().isEmpty()) {
                throw new Exception("Debe ingresar un comentario justificando el rechazo.");
            }
            solicitud.setEstado(EstadoSolicitudGasto.RECHAZADO);
            solicitud.setJefeAprobador(jefe);
            solicitud.setComentarioDecision(comentario);
            return solicitudGastoDAO.modificar(solicitud, idUsuarioAccion);
        }
    }

    private void generarTransaccionDesembolso(SolicitudGasto solicitud, String numeroOperacionBancaria, int idUsuarioAccion) throws Exception {
        CicloCajaChica ciclo = solicitud.getCiclo();
        if (ciclo == null || ciclo.getCajaChica() == null) {
            throw new Exception("La solicitud no tiene un ciclo/caja chica asignado.");
        }

        CajaChica cajaChica = ciclo.getCajaChica();
        CuentaBancaria cuentaOrigen = cajaChica.getCuentaBancaria();
        if (cuentaOrigen == null || cuentaOrigen.getIdCuenta() <= 0) {
            throw new Exception("La caja chica no tiene una cuenta bancaria configurada.");
        }

        Empleado solicitante = solicitud.getSolicitante();
        if (solicitante == null) throw new Exception("La solicitud no tiene solicitante.");

        List<CuentaBancaria> cuentasEmpleado = cuentaBancariaDAO.listarTodas().stream()
                .filter(c -> c.getEmpleadoAdministrador() != null
                        && c.getEmpleadoAdministrador().getUsuarioID() == solicitante.getUsuarioID())
                .toList();
        if (cuentasEmpleado.isEmpty()) {
            throw new Exception("El solicitante no tiene cuentas bancarias registradas.");
        }
        CuentaBancaria cuentaDestino = cuentasEmpleado.get(0);

        Transaccion transaccion = new Transaccion();
        transaccion.setTipoTransaccion(TipoTransaccion.DESEMBOLSO);
        transaccion.setFecha(new Date());
        transaccion.setMonto(solicitud.getMontoSolicitado());
        transaccion.setNumeroOperacionBancaria(numeroOperacionBancaria);
        transaccion.setMedioPago(solicitud.getMedioDesembolso() != null ? solicitud.getMedioDesembolso() : MedioPago.TRANSFERENCIA);
        transaccion.setCuentaOrigen(cuentaOrigen);
        transaccion.setCuentaDestino(cuentaDestino);
        transaccion.setMoneda(solicitud.getMonedaOriginal() != null ? solicitud.getMonedaOriginal() : cajaChica.getMoneda());
        transaccion.setBeneficiario(solicitante);
        transaccion.setEstadoTransaccion(EstadoTransaccion.COMPLETADA);

        transaccionDAO.insertar(transaccion, idUsuarioAccion);
    }

    private void calcularTotalGastado(CicloCajaChica ciclo, int idUsuarioAccion) throws Exception {
        double total = 0;
        List<SolicitudGasto> solicitudesDeGasto = solicitudGastoDAO.listarPorCiclo(ciclo.getIdCicloCaja());
        for (SolicitudGasto s : solicitudesDeGasto) {
            if (s.getEstado() == EstadoSolicitudGasto.APROBADO) {
                total += s.getMontoSolicitado();
            }
        }
        CicloCajaChica ciclito = cicloCajaChicaDAO.buscarPorId(ciclo.getIdCicloCaja());
        ciclito.setTotalGastado(total);
        cicloCajaChicaDAO.modificar(ciclito, idUsuarioAccion);
    }

}
