package pe.edu.pucp.economix.operaciones.bo;

import pe.edu.pucp.economix.operaciones.boi.ISolicitudGastoBO;
import pe.edu.pucp.economix.operaciones.dao.ISolicitudGastoDAO;
import pe.edu.pucp.economix.operaciones.implement.SolicitudGastoImplement;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;

import java.util.List;

public class SolicitudGastoBOImpl implements ISolicitudGastoBO {

    private final ISolicitudGastoDAO solicitudGastoDAO;
    public SolicitudGastoBOImpl(){
        solicitudGastoDAO= new SolicitudGastoImplement();
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
        return solicitudGastoDAO.eliminar(id);
    }

    @Override
    public SolicitudGasto buscarPorId(int id) throws Exception {
        return solicitudGastoDAO.buscarPorId(id);
    }

    @Override
    public List<SolicitudGasto> listarTodas() throws Exception {
        return solicitudGastoDAO.listarTodas();
    }

    public void validar(SolicitudGasto solicitudGasto, boolean EsModificacion) throws Exception{
        
    }
}
