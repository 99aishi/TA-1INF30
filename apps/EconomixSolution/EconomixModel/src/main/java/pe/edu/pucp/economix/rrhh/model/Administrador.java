package pe.edu.pucp.economix.rrhh.model;

public class Administrador extends Usuario{
    public Administrador(){}

    public Administrador(int usuarioID, String nombres, String apellidoPaterno, String apellidoMaterno, 
                         String password, EstadoUsuario estado, String correo) {
        super(usuarioID, nombres, apellidoPaterno, apellidoMaterno, password, estado, correo);
    }

    public Administrador(String nombres, String apellidoPaterno, String apellidoMaterno, 
                         String password, EstadoUsuario estado, String correo) {
        super(nombres, apellidoPaterno, apellidoMaterno, password, estado, correo);
    }
    public Administrador(Administrador administrador){
        super(administrador);
    }

    @Override
    public String toString() {
        return super.toString() + " Administrador{}";
    }

    public void crearUsuario(String nombre, String apellidoP, String apellidoM, String pass, EstadoUsuario estado) {
    }

    public void asignarRol(Usuario usuario, Rol rol) {
    }

    public void cambiarEstadoUsuario(Usuario usuario, EstadoUsuario nuevoEstado) {
    }
}