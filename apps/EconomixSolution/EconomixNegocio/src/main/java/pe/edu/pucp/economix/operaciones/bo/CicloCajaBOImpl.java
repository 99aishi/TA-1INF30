package pe.edu.pucp.economix.operaciones.bo;

import pe.edu.pucp.economix.operaciones.boi.ICicloCajaBO;
import pe.edu.pucp.economix.operaciones.dao.ICicloCajaChicaDAO;
import pe.edu.pucp.economix.operaciones.implement.CicloCajaChicaImplement;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.Rendicion;
import pe.edu.pucp.economix.tesoreria.bo.CajaChicaBOImpl;
import pe.edu.pucp.economix.tesoreria.boi.ICajaChicaBO;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;

import java.util.List;

public class CicloCajaBOImpl implements ICicloCajaBO {
    private final ICicloCajaChicaDAO cicloCajaChicaDAO;
    private final ICajaChicaBO cajaBO;
    public CicloCajaBOImpl(){
        cicloCajaChicaDAO= new CicloCajaChicaImplement();
        cajaBO = new CajaChicaBOImpl();
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
        ciclo.calcularTotalGastado();
        modificar(ciclo);
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
