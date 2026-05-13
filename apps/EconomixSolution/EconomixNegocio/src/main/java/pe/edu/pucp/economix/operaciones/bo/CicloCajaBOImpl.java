package pe.edu.pucp.economix.operaciones.bo;

import pe.edu.pucp.economix.operaciones.boi.ICicloCajaBO;
import pe.edu.pucp.economix.operaciones.dao.ICicloCajaChicaDAO;
import pe.edu.pucp.economix.operaciones.implement.CicloCajaChicaImplement;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.Rendicion;
import pe.edu.pucp.economix.tesoreria.bo.CajaChicaBOImpl;
import pe.edu.pucp.economix.tesoreria.boi.ICajaChicaBO;

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
        return cicloCajaChicaDAO.eliminar(id);
    }

    @Override
    public CicloCajaChica buscarPorId(int id) throws Exception {
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
        if(esModificacion && ciclo.getIdCicloCaja()<=0){
            throw new Exception("El id del Ciclo de Caja Chica es obligatorio para la modificación.");
        }

        validarCajaChica(ciclo.getCajaChica().getIdFondo());
        validarRendicion(ciclo.getRendicion());


    }
    public void validarCajaChica(int idFondo) throws Exception{
        if(idFondo<=0){
            throw new Exception("El id de la Caja Chica es obligatorio para la modificación.");
        }

        if(cajaBO.buscarPorId(idFondo)==null){
            throw new Exception("La Caja Chica no existe.");
        }
    }
    public void validarRendicion(Rendicion rendicion) throws Exception{
        if(rendicion.getIdRendicion()<=0){
            throw new Exception("El id de la Rendicion es obligatorio para la modificación.");
        }

    }
}
