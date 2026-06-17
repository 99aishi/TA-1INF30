package pe.edu.pucp.economix.operaciones.boi;

import pe.edu.pucp.economix.operaciones.ibo.ICicloCajaBO;
import pe.edu.pucp.economix.operaciones.ibo.IComprobantePagoBO;
import pe.edu.pucp.economix.operaciones.ibo.IRendicionBO;
import pe.edu.pucp.economix.operaciones.ibo.ISolicitudGastoBO;
import pe.edu.pucp.economix.operaciones.idao.IComprobantePagoDAO;
import pe.edu.pucp.economix.operaciones.idao.IRendicionDAO;
import pe.edu.pucp.economix.operaciones.daoi.RendicionDAOImpl;
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
    private final ICicloCajaBO cicloCajaBO;
    private final ISolicitudGastoBO solicitudGastoBO;
    private final IComprobantePagoBO comprobantePagoBO;
    public RendicionBOImpl(){
        rendicionDAO = new RendicionDAOImpl();
        cicloCajaBO =new CicloCajaBOImpl();
        solicitudGastoBO= new SolicitudGastoBOImpl();
        comprobantePagoBO=new ComprobantePagoBOImpl();
    }

    public Rendicion generarRendicionDeCiclo(int idCiclo, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        CicloCajaChica ciclo = cicloCajaBO.buscarPorId(idCiclo);
        cicloCajaBO.calcularTotalGastado(ciclo, idUsuarioAccion);
        if(ciclo == null){
            throw new Exception("El ciclo no existe.");
        }

        // 1. Instanciar la nueva rendición
        Rendicion rendicionCierre = new Rendicion();
        rendicionCierre.setCicloCajaChica(ciclo);
        rendicionCierre.setFechaPresentacion(new Date());
        rendicionCierre.setEstado(EstadoRendicion.EN_ESPERA);

        // 2. Ejecutar la matemática
        double totalDeclarado = calcularTotalDeclaradoValidado(ciclo);
        double totalAprobado = ciclo.getTotalGastado();
        double saldoFinal = ciclo.getSaldoInicial() - totalAprobado;

        rendicionCierre.setTotalDeclarado(totalDeclarado);
        rendicionCierre.setTotalAprobado(totalAprobado);
        rendicionCierre.setSaldoFinal(saldoFinal);

        validarMontos(rendicionCierre);

        // 4. Insertar en la base de datos
        int idGenerado = rendicionDAO.insertar(rendicionCierre, idUsuarioAccion);
        rendicionCierre.setIdRendicion(idGenerado);

        return rendicionCierre;
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
        cicloCajaBO.calcularTotalGastado(rendicion.getCicloCajaChica(), idUsuarioAccion);
        rendicion.setTotalAprobado(calcularTotalAprobado(rendicion)); // usa CICLOCAJA
        modificar(rendicion, idUsuarioAccion);
    }

    public double calcularTotalDeclaradoValidado(CicloCajaChica ciclo)throws Exception{
        double total = 0;
        List<SolicitudGasto> solicitudes = solicitudGastoBO.listarPorCiclo(ciclo.getIdCicloCaja());

        for(SolicitudGasto s : solicitudes){
            if(s.getEstado() == EstadoSolicitudGasto.APROBADO) {
                List<ComprobantePago> comprobantes = comprobantePagoBO.listarPorSolicitud(s.getIdSolicitudGasto());
                for(ComprobantePago c : comprobantes){
                    total += c.getTotal();
                }
            }
        }
        return total;
    }

    public double calcularTotalAprobado(Rendicion rendicion)throws Exception{
        double total=0;
        total = cicloCajaBO.buscarPorId(rendicion.getIdRendicion()).getTotalGastado();
        return total;
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

        if(cicloCajaBO.buscarPorId(cicloCajaChica.getIdCicloCaja())==null) {
            throw new Exception("El ciclo caja chica asignado no existe.");
        }


    }
}
