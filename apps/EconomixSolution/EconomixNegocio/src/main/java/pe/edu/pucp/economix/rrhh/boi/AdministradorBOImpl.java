package pe.edu.pucp.economix.rrhh.boi;

import pe.edu.pucp.economix.rrhh.ibo.IAdministradorBO;
import pe.edu.pucp.economix.rrhh.idao.IAdministradorDAO;
import pe.edu.pucp.economix.rrhh.daoi.AdministradorDAOImpl;
import pe.edu.pucp.economix.rrhh.model.Administrador;

import java.util.List;

public class AdministradorBOImpl implements IAdministradorBO {
    private final IAdministradorDAO administradorDAO;
    public AdministradorBOImpl(){
        administradorDAO=new AdministradorDAOImpl();
    }
    private void validarIdUsuarioAccion(int idUsuarioAccion) throws Exception {
        if (idUsuarioAccion <= 0) {
            throw new Exception("El usuario de acción debe ser mayor que cero.");
        }
    }

    @Override
    public int insertar(Administrador admin, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(admin,false);
        return administradorDAO.insertar(admin, idUsuarioAccion);
    }

    @Override
    public int modificar(Administrador admin, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(admin,true);
        return administradorDAO.modificar(admin, idUsuarioAccion);
    }

    @Override
    public int eliminar(int id, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        if (id <= 0) {
            throw new Exception("El id del administrador debe ser mayor que cero.");
        }
        return  administradorDAO.eliminar(id, idUsuarioAccion);
    }

    @Override
    public Administrador buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del administrador debe ser mayor que cero.");
        }
        return  administradorDAO.buscarPorId(id);
    }

    @Override
    public List<Administrador> listarTodas() throws Exception {
        return administradorDAO.listarTodas();
    }

    @Override
    public List<Administrador> listarActivas() throws Exception {
        return administradorDAO.listarActivas();
    }
    public void validar(Administrador admin,boolean esModificacion) throws Exception{
        if(admin==null){
            throw new Exception("El administrador no puede ser nulo.");

        }
        if (esModificacion && admin.getUsuarioID() <= 0) {
            throw new Exception("El id del administrador es obligatorio para la modificación.");
        }
        validarNombre(admin.getNombres());
        validarApellidoPaterno(admin.getApellidoPaterno());
        validarPassword(admin.getPassword());
        validarCorreo(admin.getCorreo());

    }
    public void validarNombre(String nombre) throws Exception{
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre del administrador es obligatorio.");
        }
    }
    public void validarApellidoPaterno(String apPaterno) throws Exception{
        if (apPaterno == null || apPaterno.trim().isEmpty()) {
            throw new Exception("El apellido paterno del administrador es obligatorio.");
        }
    }
    public void validarPassword(String password) throws Exception{
        if (password == null || password.trim().isEmpty()) {
            throw new Exception("El password del administrador es obligatorio.");
        }
    }
    public void validarCorreo(String correo) throws Exception{
        if (correo == null || correo.trim().isEmpty()) {
            throw new Exception("El correo del administrador es obligatorio.");
        }
    }
    public int verificarCuenta(String correo, String password) throws Exception{
        validarPassword(password);
        validarCorreo(correo);
        return  administradorDAO.verificarCuenta(correo,password);
    }
}
