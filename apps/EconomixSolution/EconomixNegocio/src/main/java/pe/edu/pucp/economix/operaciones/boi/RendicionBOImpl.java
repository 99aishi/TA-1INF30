package pe.edu.pucp.economix.operaciones.boi;

import pe.edu.pucp.economix.operaciones.ibo.IRendicionBO;
import pe.edu.pucp.economix.operaciones.idao.ICicloCajaChicaDAO;
import pe.edu.pucp.economix.operaciones.idao.IComprobantePagoDAO;
import pe.edu.pucp.economix.operaciones.idao.IRendicionDAO;
import pe.edu.pucp.economix.operaciones.idao.ISolicitudGastoDAO;
import pe.edu.pucp.economix.operaciones.daoi.CicloCajaChicaDAOImpl;
import pe.edu.pucp.economix.operaciones.daoi.ComprobantePagoDAOImpl;
import pe.edu.pucp.economix.operaciones.daoi.RendicionDAOImpl;
import pe.edu.pucp.economix.operaciones.daoi.SolicitudGastoDAOImpl;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.ComprobantePago;
import pe.edu.pucp.economix.operaciones.model.Rendicion;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoRendicion;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoSolicitudGasto;

import java.util.Date;
import java.util.List;

public class RendicionBOImpl implements IRendicionBO {

    private final IRendicionDAO rendicionDAO;
    private final ICicloCajaChicaDAO cicloCajaChicaDAO;
    private final ISolicitudGastoDAO solicitudGastoDAO;
    private final IComprobantePagoDAO comprobantePagoDAO;
    public RendicionBOImpl(){
        rendicionDAO = new RendicionDAOImpl();
        cicloCajaChicaDAO =new CicloCajaChicaDAOImpl();
        solicitudGastoDAO= new SolicitudGastoDAOImpl();
        comprobantePagoDAO=new ComprobantePagoDAOImpl();
    }

    public Rendicion generarRendicionDeCiclo(int idCiclo, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        CicloCajaChica ciclo = cicloCajaChicaDAO.buscarPorId(idCiclo);
        if (ciclo == null) {
            throw new Exception("El ciclo no existe.");
        }
        if (ciclo.getRendicion() != null && ciclo.getRendicion().getIdRendicion() > 0) {
            throw new Exception("El ciclo ya tiene una rendicion generada.");
        }

        int idGenerado = rendicionDAO.generarRendicionDeCicloSP(idCiclo, idUsuarioAccion);
        if (idGenerado <= 0) {
            throw new Exception("No se pudo generar la rendicion del ciclo.");
        }
        return rendicionDAO.buscarPorId(idGenerado);
    }
    private void validarIdUsuarioAccion(int idUsuarioAccion) throws Exception {
        if (idUsuarioAccion <= 0) {
            throw new Exception("El usuario de acción debe ser mayor que cero.");
        }
    }

    @Override
    public int insertar(Rendicion rendicion, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(rendicion,false);
        return rendicionDAO.insertar(rendicion, idUsuarioAccion);
    }

    @Override
    public int modificar(Rendicion rendicion, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(rendicion,true);
        return rendicionDAO.modificar(rendicion, idUsuarioAccion);
    }

    @Override
    public int eliminar(int id, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);

        return rendicionDAO.eliminar(id, idUsuarioAccion);
    }

    @Override
    public Rendicion buscarPorId(int id) throws Exception {
        return rendicionDAO.buscarPorId(id);
    }

    @Override
    public List<Rendicion> listarTodas() throws Exception {
        return rendicionDAO.listarTodas();
    }

    @Override
    public List<Rendicion> listarActivas() throws Exception {
        return rendicionDAO.listarActivas();
    }

    public void validar(Rendicion rendicion, boolean esModificacion) throws Exception{
        if(rendicion == null ){
            throw new Exception("La rendicion no puede ser nula.");
        }
        if(esModificacion && rendicion.getIdRendicion()<=0){
             throw new Exception("El id de la redencion es obligatorio para la modificación.");
        }

        validarCicloCajaChica(rendicion.getCicloCajaChica());
        validarMontos(rendicion);
    }

    public void calcularTotales(Rendicion rendicion, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
//        rendicion.setTotalDeclarado(calcularTotalDeclaradoValidado(rendicion));
        calcularTotalGastado(rendicion.getCicloCajaChica(), idUsuarioAccion);
        rendicion.setTotalAprobado(calcularTotalAprobado(rendicion)); // usa CICLOCAJA
        modificar(rendicion, idUsuarioAccion);
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

    public double calcularTotalDeclaradoValidado(CicloCajaChica ciclo)throws Exception{
        double total = 0;
        List<SolicitudGasto> solicitudes = solicitudGastoDAO.listarPorCiclo(ciclo.getIdCicloCaja());

        for(SolicitudGasto s : solicitudes){
            if(s.getEstado() == EstadoSolicitudGasto.APROBADO) {
                List<ComprobantePago> comprobantes = comprobantePagoDAO.listarPorSolicitud(s.getIdSolicitudGasto());
                for(ComprobantePago c : comprobantes){
                    total += c.getTotal();
                }
            }
        }
        return total;
    }

    public double calcularTotalAprobado(Rendicion rendicion)throws Exception{
        return cicloCajaChicaDAO.buscarPorId(rendicion.getCicloCajaChica().getIdCicloCaja()).getTotalGastado();
    }

    public void validarMontos(Rendicion rendicion) throws Exception {
        CicloCajaChica ciclo = rendicion.getCicloCajaChica();

        if (rendicion.getTotalDeclarado() <= 0) {
            throw new Exception("El monto total declarado debe ser mayor que cero.");
        }

        if (rendicion.getTotalAprobado() < 0) {
            throw new Exception("El monto total aprobado no puede ser negativo.");
        }

        if (rendicion.getTotalDeclarado() > ciclo.getSaldoInicial()) {
            throw new Exception("El monto rendido (" + rendicion.getTotalDeclarado() +
                    ") excede el saldo inicial del ciclo (" + ciclo.getSaldoInicial() + ").");
        }

        if (rendicion.getTotalAprobado() > rendicion.getTotalDeclarado()) {
            throw new Exception("El monto aprobado no puede ser mayor al monto declarado.");
        }

        if (rendicion.getSaldoFinal() < 0) {
            throw new Exception("El saldo final de la rendicion no puede ser negativo.");
        }
    }

    public void validarFechaPresentacion(Date fechaPresentacion)throws Exception {
        if(fechaPresentacion==null){
            throw new Exception("La rendicion tiene que tener una fecha de presentacion.");
        }
    }

    public void validarCicloCajaChica(CicloCajaChica cicloCajaChica) throws Exception{
        if(cicloCajaChica==null){
            throw new Exception("La rendicion tiene que tener un ciclo de caja chica asociado.");
        }

        if(cicloCajaChicaDAO.buscarPorId(cicloCajaChica.getIdCicloCaja())==null) {
            throw new Exception("El ciclo caja chica asignado no existe.");
        }


    }

    @Override
    public void observarRendicion(int idRendicion, String comentario, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        if (idRendicion <= 0) throw new Exception("El id de la rendicion debe ser mayor que cero.");
        if (comentario == null || comentario.trim().isEmpty()) {
            throw new Exception("Debe ingresar un comentario para observar la rendicion.");
        }
        Rendicion rendicion = rendicionDAO.buscarPorId(idRendicion);
        if (rendicion == null) throw new Exception("La rendicion no existe.");
        if (rendicion.getEstado() != EstadoRendicion.EN_ESPERA) {
            throw new Exception("Solo se puede observar una rendicion en estado EN_ESPERA.");
        }
        rendicionDAO.cambiarEstadoRendicion(idRendicion, "OBSERVADO", comentario, idUsuarioAccion);
    }

    @Override
    public void aceptarRendicion(int idRendicion, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        if (idRendicion <= 0) throw new Exception("El id de la rendicion debe ser mayor que cero.");
        Rendicion rendicion = rendicionDAO.buscarPorId(idRendicion);
        if (rendicion == null) throw new Exception("La rendicion no existe.");
        if (rendicion.getEstado() != EstadoRendicion.EN_ESPERA && rendicion.getEstado() != EstadoRendicion.OBSERVADO) {
            throw new Exception("Solo se puede aceptar una rendicion en estado EN_ESPERA u OBSERVADO.");
        }
        rendicionDAO.cambiarEstadoRendicion(idRendicion, "ACEPTADO", null, idUsuarioAccion);
    }

    @Override
    public void denegarRendicion(int idRendicion, String comentario, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        if (idRendicion <= 0) throw new Exception("El id de la rendicion debe ser mayor que cero.");
        if (comentario == null || comentario.trim().isEmpty()) {
            throw new Exception("Debe ingresar un comentario para denegar la rendicion.");
        }
        Rendicion rendicion = rendicionDAO.buscarPorId(idRendicion);
        if (rendicion == null) throw new Exception("La rendicion no existe.");
        if (rendicion.getEstado() != EstadoRendicion.EN_ESPERA && rendicion.getEstado() != EstadoRendicion.OBSERVADO) {
            throw new Exception("Solo se puede denegar una rendicion en estado EN_ESPERA u OBSERVADO.");
        }
        rendicionDAO.cambiarEstadoRendicion(idRendicion, "DENEGADO", comentario, idUsuarioAccion);
    }

    @Override
    public void reEnviarRendicion(int idRendicion, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        if (idRendicion <= 0) throw new Exception("El id de la rendicion debe ser mayor que cero.");
        Rendicion rendicion = rendicionDAO.buscarPorId(idRendicion);
        if (rendicion == null) throw new Exception("La rendicion no existe.");
        if (rendicion.getEstado() != EstadoRendicion.OBSERVADO) {
            throw new Exception("Solo se puede re-enviar una rendicion en estado OBSERVADO.");
        }
        rendicionDAO.cambiarEstadoRendicion(idRendicion, "EN_ESPERA", null, idUsuarioAccion);
    }

    @Override
    public List<Rendicion> listarPorArea(int idArea) throws Exception {
        if (idArea <= 0) throw new Exception("El id del area debe ser mayor que cero.");
        return rendicionDAO.listarPorArea(idArea);
    }
}
