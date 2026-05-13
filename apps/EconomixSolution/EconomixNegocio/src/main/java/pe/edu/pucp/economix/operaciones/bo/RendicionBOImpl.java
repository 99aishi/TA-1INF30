package pe.edu.pucp.economix.operaciones.bo;

import pe.edu.pucp.economix.operaciones.boi.IRendicionBO;
import pe.edu.pucp.economix.operaciones.dao.IRendicionDAO;
import pe.edu.pucp.economix.operaciones.implement.RendicionImplement;
import pe.edu.pucp.economix.operaciones.model.Rendicion;

import java.security.PublicKey;
import java.util.List;

public class RendicionBOImpl implements IRendicionBO {

    private final IRendicionDAO rendicionDAO;

    public RendicionBOImpl(){
        rendicionDAO = new RendicionImplement();
    }
    @Override
    public int insertar(Rendicion rendicion) throws Exception {
        validar(rendicion,false);
        return rendicionDAO.insertar(rendicion);
    }

    @Override
    public int modificar(Rendicion rendicion) throws Exception {
        validar(rendicion,true);
        return rendicionDAO.modificar(rendicion);
    }

    @Override
    public int eliminar(int id) throws Exception {

        return rendicionDAO.eliminar(id);
    }

    @Override
    public Rendicion buscarPorId(int id) throws Exception {
        return rendicionDAO.buscarPorId(id);
    }

    @Override
    public List<Rendicion> listarTodas() throws Exception {
        return rendicionDAO.listarTodas();
    }

    public void validar(Rendicion rendicion, boolean esModificacion) throws Exception{
        if(rendicion == null ){
            throw new Exception("La rendicion no puede ser nula.");
        }
        if(esModificacion && rendicion.getIdRendicion()<=0){
             throw new Exception("El id de la redencion es obligatorio para la modificación.");
        }

        
    }
}
