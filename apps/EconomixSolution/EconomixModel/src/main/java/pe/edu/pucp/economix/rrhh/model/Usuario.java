package pe.edu.pucp.economix.rrhh.model;

public abstract class Usuario{
    private int usuarioID;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String password;
    private EstadoUsuario estado;

    //Constructores
    public Usuario(){}

    public Usuario(int usuarioID, String nombres, String apellidoPaterno, String apellidoMaterno, String password, EstadoUsuario estado) {
        this.usuarioID = usuarioID;
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.password = password;
        this.estado = estado;
    }
    public Usuario(String nombres, String apellidoPaterno, String apellidoMaterno, String password, EstadoUsuario estado) {
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.password = password;
        this.estado = estado;
    }

    //Selectores
    public int getUsuarioID(){
        return usuarioID;
    }
    public void setUsuarioID(int usuarioID){
        this.usuarioID=usuarioID;
    }
    public String getNombres(){
        return nombres;
    }
    public void setNombres(String nombres){
        this.nombres = nombres;
    }
    public String getApellidoPaterno(){
        return apellidoPaterno;
    }
    public void setApellidoPaterno(String apellido_paterno){
        this.apellidoPaterno=apellido_paterno;
    }
    public String getApellidoMaterno(){
        return apellidoMaterno;
    }
    public void setApellidoMaterno(String apellido_materno){
        this.apellidoMaterno=apellido_materno;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public void cambiarPassword(String nueva){
        this.password=nueva;
    }
    public EstadoUsuario getEstado() {
        return estado;
    }
    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }

    //Metodos

    @Override
    public String toString() {
        return super.toString() + "Usuario{" +
                "usuarioID=" + usuarioID +
                ", nombres='" + nombres + '\'' +
                ", apellidoPaterno='" + apellidoPaterno + '\'' +
                ", apellidoMaterno='" + apellidoMaterno + '\'' +
                ", password='" + password + '\'' +
                ", estado=" + estado +
                '}';
    }

    public boolean login(int idIngresado, String passIngresada){
        if(usuarioID == idIngresado)
            if(estado == EstadoUsuario.Activo){
                //return verificarPassword(passIngresada,this.password)

                if(passIngresada.equals(this.password)) return true;
            }
        return false;
    }
    public String cifrarPassword(String passwordPlano) {
        // TODO
        return "";
    }

    public boolean validarAccesoPorEstado() {
        // TODO
        return false;
    }
}
