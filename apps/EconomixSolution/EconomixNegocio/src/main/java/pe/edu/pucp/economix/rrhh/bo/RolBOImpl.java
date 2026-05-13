package pe.edu.pucp.economix.rrhh.bo;

import pe.edu.pucp.economix.rrhh.boi.IRolBO;
import pe.edu.pucp.economix.rrhh.dao.IRolDAO;
import pe.edu.pucp.economix.rrhh.implement.RolImplement;
import pe.edu.pucp.economix.rrhh.model.Rol;

import java.util.List;

public class RolBOImpl implements IRolBO {
    private final IRolDAO rolDAO;
    public RolBOImpl(){
        rolDAO=new RolImplement();
    }
    @Override
    public int insertar(Rol rol) throws Exception {
        validar(rol,false);
        return rolDAO.insertar(rol);
    }

    @Override
    public int modificar(Rol rol) throws Exception {
        validar(rol,true);
        return rolDAO.modificar(rol);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del rol debe ser mayor que cero.");
        }
        return  rolDAO.eliminar(id);
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
