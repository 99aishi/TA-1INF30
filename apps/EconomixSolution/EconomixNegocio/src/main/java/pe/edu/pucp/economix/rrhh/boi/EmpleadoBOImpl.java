package pe.edu.pucp.economix.rrhh.boi;

import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.rrhh.ibo.IEmpleadoBO;
import pe.edu.pucp.economix.rrhh.idao.IAreaDAO;
import pe.edu.pucp.economix.rrhh.idao.IEmpleadoDAO;
import pe.edu.pucp.economix.rrhh.idao.IRolDAO;
import pe.edu.pucp.economix.rrhh.daoi.AreaDAOImpl;
import pe.edu.pucp.economix.rrhh.daoi.EmpleadoDAOImpl;
import pe.edu.pucp.economix.rrhh.daoi.RolDAOImpl;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.rrhh.model.EstadoUsuario;
import pe.edu.pucp.economix.rrhh.model.Rol;
import pe.edu.pucp.economix.rrhh.model.RolFlujo;

import java.sql.SQLException;
import java.util.List;

public class EmpleadoBOImpl implements IEmpleadoBO {
    private final IEmpleadoDAO empleadoDAO;
    private final IRolDAO rolDAO;
    private final IAreaDAO areaDAO;
    public EmpleadoBOImpl(){
        empleadoDAO=new EmpleadoDAOImpl();
        rolDAO=new RolDAOImpl();
        areaDAO=new AreaDAOImpl();
    }
    private void validarIdUsuarioAccion(int idUsuarioAccion) throws Exception {
        if (idUsuarioAccion <= 0) {
            throw new Exception("El usuario de acción debe ser mayor que cero.");
        }
    }

    @Override
    public int insertar(Empleado empleado, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(empleado,false);

        Area area = areaDAO.buscarPorId(empleado.getArea().getIdArea());
        if (area == null) {
            throw new Exception("El área del empleado no existe.");
        }

        Empleado jefeActual = determinarJefeActivo(area);

        if (jefeActual == null) {
            // Primer empleado activo del área: se convierte en jefe
            empleado.setRolFlujo(RolFlujo.JEFE_AREA);
            empleado.setJefeDirecto(null);
            System.out.println("[EmpleadoBO] Area " + area.getNombre() + " sin jefe activo. Nuevo empleado sera JEFE_AREA.");
        } else {
            // El área ya tiene jefe activo: el nuevo empleado reporta a él
            empleado.setRolFlujo(RolFlujo.EMPLEADO);
            empleado.setJefeDirecto(jefeActual);
            System.out.println("[EmpleadoBO] Area " + area.getNombre() + " tiene jefe " + jefeActual.getUsuarioID()
                    + ". Nuevo empleado reporta a ese jefe.");
        }

        int id = empleadoDAO.insertar(empleado, idUsuarioAccion);

        if (empleado.getRolFlujo() == RolFlujo.JEFE_AREA) {
            areaDAO.asignarJefe(area, empleado, idUsuarioAccion);
            System.out.println("[EmpleadoBO] Asignado empleado " + id + " como jefe del area " + area.getIdArea());
        }

        return id;
    }

    private Empleado determinarJefeActivo(Area area) {
        Empleado jefe = area.getJefe();
        if (jefe == null) return null;
        // Consideramos activo si tiene estado (cargado por el DAO) o si no se cargo estado asumimos activo
        if (jefe.getEstado() != null && jefe.getEstado() != EstadoUsuario.ACTIVO) {
            System.out.println("[EmpleadoBO] Jefe existente del area " + area.getIdArea() + " esta inactivo. Se tratara como sin jefe.");
            return null;
        }
        return jefe;
    }

    @Override
    public int modificar(Empleado empleado, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(empleado,true);
        return empleadoDAO.modificar(empleado, idUsuarioAccion);
    }

    @Override
    public int eliminar(int id, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        if (id <= 0) {
            throw new Exception("El id del empleado debe ser mayor que cero.");
        }
        return  empleadoDAO.eliminar(id, idUsuarioAccion);
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

    @Override
    public List<Empleado> listarActivas() throws Exception {
        return empleadoDAO.listarActivas();
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
        validarNombre(empleado.getNombres());
        validarApellidoPaterno(empleado.getApellidoPaterno());
        validarPassword(empleado.getPassword());
        validarCorreo(empleado.getCorreo());
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
    public int verificarCuenta(String correo, String password) throws Exception{
        validarPassword(password);
        validarCorreo(correo);
        return empleadoDAO.verificarCuenta(correo,password);
    }

    public List<Empleado> listarPorNombreApellido(String busqueda) throws Exception{
        return empleadoDAO.listarPorNombreApellido(busqueda);
    }
}
