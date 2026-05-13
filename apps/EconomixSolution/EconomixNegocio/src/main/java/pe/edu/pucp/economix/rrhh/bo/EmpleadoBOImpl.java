package pe.edu.pucp.economix.rrhh.bo;

import pe.edu.pucp.economix.rrhh.boi.IEmpleadoBO;
import pe.edu.pucp.economix.rrhh.dao.IAreaDAO;
import pe.edu.pucp.economix.rrhh.dao.IEmpleadoDAO;
import pe.edu.pucp.economix.rrhh.dao.IRolDAO;
import pe.edu.pucp.economix.rrhh.implement.AreaImplement;
import pe.edu.pucp.economix.rrhh.implement.EmpleadoImplement;
import pe.edu.pucp.economix.rrhh.implement.RolImplement;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.rrhh.model.Rol;

import java.util.List;

public class EmpleadoBOImpl implements IEmpleadoBO {
    private final IEmpleadoDAO empleadoDAO;
    private final IRolDAO rolDAO;
    private final IAreaDAO areaDAO;
    public EmpleadoBOImpl(){
        empleadoDAO=new EmpleadoImplement();
        rolDAO=new RolImplement();
        areaDAO=new AreaImplement();
    }
    @Override
    public int insertar(Empleado empleado) throws Exception {
        validar(empleado,false);
        return empleadoDAO.insertar(empleado);
    }

    @Override
    public int modificar(Empleado empleado) throws Exception {
        validar(empleado,true);
        return empleadoDAO.modificar(empleado);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del empleado debe ser mayor que cero.");
        }
        return  empleadoDAO.eliminar(id);
    }

    @Override
    public Empleado buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del empleado debe ser mayor que cero.");
        }
        return  empleadoDAO.buscarPorId(id);
    }

    @Override
    public List<Empleado> listarTodas() throws Exception {
        return empleadoDAO.listarTodas();
    }
    public void validar(Empleado empleado,boolean esModificacion) throws Exception{
        if(empleado==null){
            throw new Exception("El empleado no puede ser nulo.");

        }
        if (esModificacion && empleado.getUsuarioID() <= 0) {
            throw new Exception("El id del empleado es obligatorio para la modificación.");
        }
        validarRol(empleado.getRol());
        validarArea(empleado.getArea());
        if(empleadoDAO.listarTodas()!=null) {
            validarJefe(empleado.getJefeDirecto());
        }
        validarNombre(empleado.getNombres());
        validarApellidoPaterno(empleado.getApellidoPaterno());
        validarApellidoMaterno(empleado.getApellidoMaterno());
        validarPassword(empleado.getPassword());
        validarCorreo(empleado.getCorreoInstitucional());
        validarNumeroCelular(empleado.getNumeroCelular());
    }
    public void validarRol(Rol rol)throws Exception{
        if (rol == null) {
            throw new Exception("El rol del empleado obligatorio.");
        }

        if (rol.getRolID() <= 0) {
            throw new Exception("El rol del empleado no es válido.");
        }
        if(rolDAO.buscarPorId(rol.getRolID())==null){
            throw new Exception("El rol del empleado no existe.");
        }
    }
    public void validarArea(Area area) throws Exception{
        if (area == null) {
            throw new Exception("El area del empleado es obligatorio.");
        }

        if (area.getIdArea() <= 0) {
            throw new Exception("El area del empleado no es válido.");
        }
        if(areaDAO.buscarPorId(area.getIdArea())==null){
            throw new Exception("El area del empleado no existe.");
        }
    }
    public void validarJefe(Empleado jefe) throws Exception {

        if (jefe == null) {
                throw new Exception("El jefe del empleado es obligatorio.");
        }

        if (jefe.getUsuarioID() <= 0) {
            throw new Exception("El jefe del empleado no es válido.");
        }
        if (empleadoDAO.buscarPorId(jefe.getUsuarioID()) == null) {
            throw new Exception("El jefe del empleado no existe.");
        }

    }
    public void validarNombre(String nombre) throws Exception{
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre del empleado es obligatorio.");
        }
    }
    public void validarApellidoMaterno(String apMaterno) throws Exception{
        if (apMaterno == null || apMaterno.trim().isEmpty()) {
            throw new Exception("El apellido materno del empleado es obligatorio.");
        }
    }
    public void validarApellidoPaterno(String apPaterno) throws Exception{
        if (apPaterno == null || apPaterno.trim().isEmpty()) {
            throw new Exception("El apellido paterno del empleado es obligatorio.");
        }
    }
    public void validarPassword(String password) throws Exception{
        if (password == null || password.trim().isEmpty()) {
            throw new Exception("El password del empleado es obligatorio.");
        }
    }
    public void validarCorreo(String correo) throws Exception{
        if (correo == null || correo.trim().isEmpty()) {
            throw new Exception("El correo del empleado es obligatorio.");
        }
    }
    public void validarNumeroCelular(String celular)throws Exception{
        if (celular== null || celular.trim().isEmpty()) {
            throw new Exception("El numero de celular del empleado es obligatorio.");
        }

        celular= celular.trim();

        if (celular.length() < 9) {
            throw new Exception("El numero de celular del empleado debe tener al menos 9 caracteres.");
        }

        if (celular.length() > 9) {
            throw new Exception("El numero de celularr del empleado no puede exceder los 9 caracteres.");
        }
    }
}
