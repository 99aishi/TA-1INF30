package pe.edu.pucp.economix.operaciones.boi;

import pe.edu.pucp.economix.operaciones.ibo.ICicloCajaBO;
import pe.edu.pucp.economix.operaciones.ibo.ISolicitudGastoBO;
import pe.edu.pucp.economix.operaciones.idao.ICicloCajaChicaDAO;
import pe.edu.pucp.economix.operaciones.daoi.CicloCajaChicaDAOImpl;
import pe.edu.pucp.economix.operaciones.idao.ISolicitudGastoDAO;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoSolicitudGasto;
import pe.edu.pucp.economix.tesoreria.boi.CajaChicaBOImpl;
import pe.edu.pucp.economix.tesoreria.ibo.ICajaChicaBO;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;

import java.util.List;

public class CicloCajaBOImpl implements ICicloCajaBO {
    private final ICicloCajaChicaDAO cicloCajaChicaDAO;
    private final ICajaChicaBO cajaBO;
    private final ISolicitudGastoBO solicitudGastoBO;
    public CicloCajaBOImpl(){

        cicloCajaChicaDAO= new CicloCajaChicaDAOImpl();
        cajaBO = new CajaChicaBOImpl();
        solicitudGastoBO=new SolicitudGastoBOImpl();
    }

    @Override
    public int insertar(CicloCajaChica ciclo) throws Exception {
        validar(ciclo,false);
        return cicloCajaChicaDAO.insertar(ciclo);
    }

    @Override
    public int modificar(CicloCajaChica ciclo) throws Exception {
        validar(ciclo,true);
        return cicloCajaChicaDAO.modificar(ciclo);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if(id<=0){
            throw new Exception("Debe ingresar el ID del ciclo.");
        }
        return cicloCajaChicaDAO.eliminar(id);
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
    public void calcularTotalGastado(CicloCajaChica ciclo) throws Exception {

        double total=0;
        List<SolicitudGasto> solicitudesDeGasto= solicitudGastoBO.listarPorCiclo(ciclo.getIdCicloCaja());
        for (SolicitudGasto s: solicitudesDeGasto){
            if(s.getEstado()== EstadoSolicitudGasto.Aprobado){
                total+=s.getMontoSolicitado();
            }
        }
        CicloCajaChica ciclito= buscarPorId(ciclo.getIdCicloCaja());
        ciclito.setTotalGastado(total);
        modificar(ciclito);
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


}
