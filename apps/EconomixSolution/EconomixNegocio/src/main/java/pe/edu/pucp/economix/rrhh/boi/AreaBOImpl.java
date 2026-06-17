package pe.edu.pucp.economix.rrhh.boi;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.rrhh.ibo.IAreaBO;
import pe.edu.pucp.economix.rrhh.idao.IAreaDAO;
import pe.edu.pucp.economix.rrhh.idao.IEmpleadoDAO;
import pe.edu.pucp.economix.rrhh.daoi.AreaDAOImpl;
import pe.edu.pucp.economix.rrhh.daoi.EmpleadoDAOImpl;
import pe.edu.pucp.economix.rrhh.model.Area;

import java.util.List;

public class AreaBOImpl implements IAreaBO {
    private final IAreaDAO areaDAO;
    private final IEmpleadoDAO empleadoDAO;
    public AreaBOImpl(){
        areaDAO=new AreaDAOImpl();
        empleadoDAO=new EmpleadoDAOImpl();
    }
    private void validarIdUsuarioAccion(int idUsuarioAccion) throws Exception {
        if (idUsuarioAccion <= 0) {
            throw new Exception("El usuario de acción debe ser mayor que cero.");
        }
    }

    @Override
    public int insertar(Area area, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(area,false);
        return areaDAO.insertar(area, idUsuarioAccion);
    }

    @Override
    public int modificar(Area area, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(area,true);
        return areaDAO.modificar(area, idUsuarioAccion);
    }

    @Override
    public int eliminar(int id, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        if (id <= 0) {
            throw new Exception("El id del area debe ser mayor que cero.");
        }
        return  areaDAO.eliminar(id, idUsuarioAccion);
    }

    @Override
    public Area buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del area debe ser mayor que cero.");
        }
        return areaDAO.buscarPorId(id);
    }

    @Override
    public List<Area> listarTodas() throws Exception {
        return areaDAO.listarTodas();
    }

    @Override
    public List<Area> listarActivas() throws Exception {
        return areaDAO.listarActivas();
    }

    @Override
    public int recuperar(int id, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        if (id <= 0) {
            throw new Exception("El id del area debe ser mayor que cero.");
        }
        return areaDAO.recuperar(id, idUsuarioAccion);
    }

    public void validar(Area area, boolean esModificacion) throws Exception{
        if(area==null){
            throw new Exception("El area no puede ser nulo.");

        }
        if (esModificacion && area.getIdArea() <= 0) {
            throw new Exception("El id del area es obligatorio para la modificación.");
        }

        validarNombre(area.getNombre());
        validarDescripcion(area.getDescripcion());

    }


    public void validarNombre(String nombre) throws Exception{
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre del area es obligatorio.");
        }
    }
    public void validarDescripcion(String descripcion) throws Exception{
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new Exception("La descripcion del area es obligatoria.");
        }
    }
}
