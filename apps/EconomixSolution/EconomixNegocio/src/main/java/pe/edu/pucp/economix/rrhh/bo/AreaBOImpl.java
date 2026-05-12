package pe.edu.pucp.economix.rrhh.bo;

import pe.edu.pucp.economix.rrhh.boi.IAreaBO;
import pe.edu.pucp.economix.rrhh.boi.IEmpleadoBO;
import pe.edu.pucp.economix.rrhh.dao.IAreaDAO;
import pe.edu.pucp.economix.rrhh.dao.IEmpleadoDAO;
import pe.edu.pucp.economix.rrhh.implement.AreaImplement;
import pe.edu.pucp.economix.rrhh.implement.EmpleadoImplement;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.tesoreria.boi.ICajaChicaBO;
import pe.edu.pucp.economix.tesoreria.dao.ICajaChicaDAO;
import pe.edu.pucp.economix.tesoreria.implement.CajaChicaImplement;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;

import java.util.List;

public class AreaBOImpl implements IAreaBO {
    private final IAreaDAO areaDAO;
    private final IEmpleadoDAO empleadoDAO;
    private final ICajaChicaDAO cajaChicaDAO;
    public AreaBOImpl(){
        areaDAO=new AreaImplement();
        empleadoDAO=new EmpleadoImplement();
        cajaChicaDAO=new CajaChicaImplement();
    }
    @Override
    public int insertar(Area area) throws Exception {
        validar(area,false);
        return areaDAO.insertar(area);
    }

    @Override
    public int modificar(Area area) throws Exception {
        validar(area,true);
        return areaDAO.modificar(area);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del area debe ser mayor que cero.");
        }
        return  areaDAO.eliminar(id);
    }

    @Override
    public Area buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del area debe ser mayor que cero.");
        }
        return  areaDAO.buscarPorId(id);
    }

    @Override
    public List<Area> listarTodas() throws Exception {
        return List.of();
    }
    public void validar(Area area, boolean esModificacion) throws Exception{
        if(area==null){
            throw new Exception("El area no puede ser nulo.");

        }
        if (esModificacion && area.getIdArea() <= 0) {
            throw new Exception("El id del area es obligatorio para la modificación.");
        }
        validarJefe(area.getJefe());
        validarNombre(area.getNombre());
        validarDescripcion(area.getDescripcion());

    }
    public void validarJefe(Empleado jefe) throws Exception{
        if (jefe == null) {
            throw new Exception("El jefe del area es obligatorio.");
        }

        if (jefe.getUsuarioID() <= 0) {
            throw new Exception("El jefe del area no es válido.");
        }
        if(empleadoDAO.buscarPorId(jefe.getUsuarioID())==null){
            throw new Exception("El jefe del area no existe.");
        }
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
