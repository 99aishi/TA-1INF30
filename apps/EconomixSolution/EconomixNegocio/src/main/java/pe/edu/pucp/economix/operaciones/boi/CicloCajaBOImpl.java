package pe.edu.pucp.economix.operaciones.boi;

import pe.edu.pucp.economix.operaciones.ibo.ICicloCajaBO;
import pe.edu.pucp.economix.operaciones.ibo.ISolicitudGastoBO;
import pe.edu.pucp.economix.operaciones.ibo.ITransaccionBO;
import pe.edu.pucp.economix.operaciones.idao.ICicloCajaChicaDAO;
import pe.edu.pucp.economix.operaciones.daoi.CicloCajaChicaDAOImpl;
import pe.edu.pucp.economix.operaciones.idao.ISolicitudGastoDAO;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.ComprobantePago;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.Transaccion;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoComprobante;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoCicloCaja;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoSolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoTransaccion;
import pe.edu.pucp.economix.operaciones.model.enums.MedioPago;
import pe.edu.pucp.economix.operaciones.model.enums.TipoTransaccion;
import pe.edu.pucp.economix.tesoreria.boi.CajaChicaBOImpl;
import pe.edu.pucp.economix.tesoreria.boi.CuentaBancariaBOImpl;
import pe.edu.pucp.economix.tesoreria.ibo.ICajaChicaBO;
import pe.edu.pucp.economix.tesoreria.ibo.ICuentaBancariaBO;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.util.Date;
import java.util.List;

public class CicloCajaBOImpl implements ICicloCajaBO {
    private final ICicloCajaChicaDAO cicloCajaChicaDAO;
    private final ICajaChicaBO cajaBO;
    private final ISolicitudGastoBO solicitudGastoBO;
    private final ITransaccionBO transaccionBO;
    private final ICuentaBancariaBO cuentaBancariaBO;
    public CicloCajaBOImpl(){

        cicloCajaChicaDAO= new CicloCajaChicaDAOImpl();
        cajaBO = new CajaChicaBOImpl();
        solicitudGastoBO=new SolicitudGastoBOImpl();
        transaccionBO=new TransaccionBOImpl();
        cuentaBancariaBO=new CuentaBancariaBOImpl();
    }

    private void validarIdUsuarioAccion(int idUsuarioAccion) throws Exception {
        if (idUsuarioAccion <= 0) {
            throw new Exception("El usuario de acción debe ser mayor que cero.");
        }
    }

    @Override
    public int insertar(CicloCajaChica ciclo, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(ciclo,false);
        return cicloCajaChicaDAO.insertar(ciclo, idUsuarioAccion);
    }

    @Override
    public int modificar(CicloCajaChica ciclo, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(ciclo,true);
        return cicloCajaChicaDAO.modificar(ciclo, idUsuarioAccion);
    }

    @Override
    public int eliminar(int id, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        if(id<=0){
            throw new Exception("Debe ingresar el ID del ciclo.");
        }
        return cicloCajaChicaDAO.eliminar(id, idUsuarioAccion);
    }

    @Override
    public CicloCajaChica buscarPorId(int id) throws Exception {
        if(id<=0){
            throw new Exception("Debe ingresar el ID del ciclo.");
        }
        return cicloCajaChicaDAO.buscarPorId(id);
    }

    @Override
    public List<CicloCajaChica> listarTodas() throws Exception {
        return cicloCajaChicaDAO.listarTodas();
    }

    @Override
    public List<CicloCajaChica> listarActivos() throws Exception {
        return cicloCajaChicaDAO.listarActivos();
    }

    public void validar (CicloCajaChica ciclo, boolean esModificacion) throws Exception{
        if(ciclo == null ){
            throw new Exception("El ciclo de caja chica no puede ser nulo.");
        }
        if(esModificacion && ciclo.getIdCicloCaja()<=0) {
            throw new Exception("El id del Ciclo de Caja Chica es obligatorio para la modificación.");
        }
        validarCajaChica(ciclo.getCajaChica());
        validarSemana(ciclo.getNumeroSemana());
        validarMontos(ciclo);
    }
    public void calcularTotalGastado(CicloCajaChica ciclo, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);

        double total=0;
        List<SolicitudGasto> solicitudesDeGasto= solicitudGastoBO.listarPorCiclo(ciclo.getIdCicloCaja());
        for (SolicitudGasto s: solicitudesDeGasto){
            if(s.getEstado()== EstadoSolicitudGasto.APROBADO){
                total+=s.getMontoSolicitado();
            }
        }
        CicloCajaChica ciclito= buscarPorId(ciclo.getIdCicloCaja());
        ciclito.setTotalGastado(total);
        modificar(ciclito, idUsuarioAccion);
    }


    public void validarMontos(CicloCajaChica ciclo) throws Exception{
        if(ciclo.getSaldoInicial()<0){
            throw new Exception("El saldo inicial del Ciclo de Caja Chica no puede ser negativo.");
        }
        if(ciclo.getTotalGastado()<0){
            throw new Exception("El total gastado del Ciclo de Caja Chica no puede ser negativo.");
        }
    }

    public void validarSemana(int numeroSemana)throws Exception{
        if(numeroSemana<=0){
            throw new Exception("El Ciclo de Caja Chica debe tener un numero de semana valido.");
        }
    }

    public void validarCajaChica(CajaChica cajaChica) throws Exception{
        if(cajaChica==null){
            throw new Exception("El ciclo debe estar asignado a una Caja Chica.");
        }
        if(cajaChica.getIdFondo()<=0){
            throw new Exception("El id de la Caja Chica es obligatorio para la modificación.");
        }
        if(cajaBO.buscarPorId(cajaChica.getIdFondo())==null){
            throw new Exception("La Caja Chica no existe.");
        }
    }

    @Override
    public int cerrarCiclo(int idCicloCaja, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        if (idCicloCaja <= 0) throw new Exception("El id del ciclo debe ser mayor que cero.");

        CicloCajaChica ciclo = cicloCajaChicaDAO.buscarPorId(idCicloCaja);
        if (ciclo == null) throw new Exception("El ciclo de caja chica no existe.");
        if (ciclo.getEstado() == EstadoCicloCaja.CERRADO || ciclo.getEstado() == EstadoCicloCaja.LIQUIDADO) {
            throw new Exception("El ciclo ya está cerrado o liquidado.");
        }

        calcularTotalGastado(ciclo, idUsuarioAccion);

        CajaChica cajaChica = ciclo.getCajaChica();
        if (cajaChica == null) throw new Exception("El ciclo no tiene caja chica asignada.");

        CuentaBancaria cuentaArea = cajaChica.getCuentaBancaria();
        if (cuentaArea == null || cuentaArea.getIdCuenta() <= 0) {
            throw new Exception("La caja chica no tiene cuenta bancaria asignada.");
        }

        // Calcular monto consumido real a partir de comprobantes aprobados
        double totalComprobantesAprobados = 0;
        List<SolicitudGasto> solicitudes = solicitudGastoBO.listarPorCiclo(idCicloCaja);
        for (SolicitudGasto s : solicitudes) {
            if (s.getEstado() == EstadoSolicitudGasto.APROBADO || s.getEstado() == EstadoSolicitudGasto.PAGADO || s.getEstado() == EstadoSolicitudGasto.RENDIDO) {
                List<ComprobantePago> comprobantes = listarComprobantesPorSolicitud(s.getIdSolicitudGasto());
                for (ComprobantePago c : comprobantes) {
                    if (c.getEstado() == EstadoComprobante.APROBADO) {
                        totalComprobantesAprobados += c.getTotal();
                    }
                }
            }
        }

        double totalDesembolsado = ciclo.getTotalGastado(); // aprobado
        double saldoFinal = totalDesembolsado - totalComprobantesAprobados;

        if (saldoFinal > 0) {
            // El empleado gastó menos de lo desembolsado -> devolución de sobrante
            generarTransaccionCierre(TipoTransaccion.DEVOLUCION_SOBRANTE, saldoFinal, cuentaArea, null, cajaChica.getMoneda(), idUsuarioAccion);
        } else if (saldoFinal < 0) {
            // El empleado gastó más de lo desembolsado -> reembolso por déficit
            generarTransaccionCierre(TipoTransaccion.REEMBOLSO_DEFICIT, Math.abs(saldoFinal), cuentaArea, null, cajaChica.getMoneda(), idUsuarioAccion);
        }

        ciclo.setEstado(EstadoCicloCaja.CERRADO);
        ciclo.setFechaCierre(new Date());
        return cicloCajaChicaDAO.modificar(ciclo, idUsuarioAccion);
    }

    private List<ComprobantePago> listarComprobantesPorSolicitud(int idSolicitud) throws Exception {
        // Acceso directo al DAO para evitar referencia circular
        return new pe.edu.pucp.economix.operaciones.daoi.ComprobantePagoDAOImpl().listarPorSolicitud(idSolicitud);
    }

    private void generarTransaccionCierre(TipoTransaccion tipo, double monto, CuentaBancaria cuentaArea, CuentaBancaria cuentaEmpleado, Moneda moneda, int idUsuarioAccion) throws Exception {
        Transaccion transaccion = new Transaccion();
        transaccion.setTipoTransaccion(tipo);
        transaccion.setFecha(new Date());
        transaccion.setMonto(monto);
        transaccion.setNumeroOperacionBancaria("AUTO-" + System.currentTimeMillis());
        transaccion.setMedioPago(MedioPago.TRANSFERENCIA);

        if (tipo == TipoTransaccion.DEVOLUCION_SOBRANTE) {
            // El empleado devuelve a la cuenta del área
            transaccion.setCuentaOrigen(cuentaEmpleado);
            transaccion.setCuentaDestino(cuentaArea);
        } else {
            // El área reembolsa el déficit
            transaccion.setCuentaOrigen(cuentaArea);
            transaccion.setCuentaDestino(cuentaEmpleado);
        }

        transaccion.setMoneda(moneda);
        transaccion.setEstadoTransaccion(EstadoTransaccion.COMPLETADA);
        transaccionBO.insertar(transaccion, idUsuarioAccion);
    }

}
