package pe.edu.pucp.economix.rrhh.model;

public class Administrador extends Usuario{
    private String correoSoporte;

    //Constructores
    public Administrador(){}

    public Administrador(int usuarioID, String nombres, String apellidoPaterno, String apellidoMaterno, String password, EstadoUsuario estado, String correoSoporte) {
        super(usuarioID, nombres, apellidoPaterno, apellidoMaterno, password, estado);
        this.correoSoporte = correoSoporte;
    }

    public Administrador(String nombres, String apellidoPaterno, String apellidoMaterno, String password, EstadoUsuario estado, String correoSoporte) {
        super(nombres, apellidoPaterno, apellidoMaterno, password, estado);
        this.correoSoporte = correoSoporte;
    }

    //Selectores
    public String getCorreoSoporte() {
        return correoSoporte;
    }
    public void setCorreoSoporte(String correoSoporte) {
        this.correoSoporte = correoSoporte;
    }

    // Métodos


    @Override
    public String toString() {
        return super.toString() +  "Administrador{" +
                "correoSoporte='" + correoSoporte + '\'' +
                '}';
    }

    public void crearUsuario(String nombre, String apellidoP, String apellidoM, String pass, EstadoUsuario estado) {
        // TODO: Implementar creación de registro en BD (RF_04)
    }

    public void asignarRol(Usuario usuario, Rol rol) {
        // TODO: Lógica para obligar asignación de rol único (RF_02)
    }

    public void cambiarEstadoUsuario(Usuario usuario, EstadoUsuario nuevoEstado) {
        // TODO: Implementar desactivación/activación de cuentas (RF_04)
    }
}
