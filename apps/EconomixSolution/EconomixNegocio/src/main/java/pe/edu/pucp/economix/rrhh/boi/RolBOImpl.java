package pe.edu.pucp.economix.rrhh.boi;

import pe.edu.pucp.economix.rrhh.ibo.IRolBO;
import pe.edu.pucp.economix.rrhh.idao.IRolDAO;
import pe.edu.pucp.economix.rrhh.daoi.RolDAOImpl;
import pe.edu.pucp.economix.rrhh.model.Rol;

import java.util.List;

public class RolBOImpl implements IRolBO {
    private final IRolDAO rolDAO;
    public RolBOImpl(){
        rolDAO=new RolDAOImpl();
    }
    private void validarIdUsuarioAccion(int idUsuarioAccion) throws Exception {
        if (idUsuarioAccion <= 0) {
            throw new Exception("El usuario de acción debe ser mayor que cero.");
        }
    }

    @Override
    public int insertar(Rol rol, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(rol,false);
        return rolDAO.insertar(rol, idUsuarioAccion);
    }

    @Override
    public int modificar(Rol rol, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(rol,true);
        return rolDAO.modificar(rol, idUsuarioAccion);
    }

    @Override
    public int eliminar(int id, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        if (id <= 0) {
            throw new Exception("El id del rol debe ser mayor que cero.");
        }
        return  rolDAO.eliminar(id, idUsuarioAccion);
    }

    @Override
    public Rol buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del rol debe ser mayor que cero.");
        }
        return  rolDAO.buscarPorId(id);
    }

    @Override
    public List<Rol> listarTodas() throws Exception {
        return rolDAO.listarTodas();
    }
    public void validar(Rol rol, boolean esModificacion)throws Exception{
        if(rol==null){
            throw new Exception("El rol no puede ser nulo.");

        }
        if (esModificacion && rol.getRolID() <= 0) {
            throw new Exception("El id del rol es obligatorio para la modificación.");
        }
        validarTitulo(rol.getTitulo());
        validarDescripcion(rol.getDescripcion());
    }
    public void validarTitulo(String titulo) throws Exception{
        if (titulo== null || titulo.trim().isEmpty()) {
            throw new Exception("El titulo del rol es obligatorio.");
        }
    }
    public void validarDescripcion(String descripcion) throws Exception{
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new Exception("La descripcion del rol es obligatoria.");
        }
    }
}
