package pe.edu.pucp.economix.rrhh.model;

public class Administrador extends Usuario{
    private String correoSoporte;

    //Constructores
    public Administrador(){

    }
    public Administrador(String nombre, String apellido_paterno,
                         String apellido_materno, String password, EstadoUsuario estado,
                         String correoSoporte){
        super(nombre,  apellido_paterno, apellido_materno,  password, estado);

        this.correoSoporte=correoSoporte;
    }


    //Selectores
    public String getCorreoSoporte() {
        return correoSoporte;
    }
    public void setCorreoSoporte(String correoSoporte) {
        this.correoSoporte = correoSoporte;
    }

    // Métodos
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
