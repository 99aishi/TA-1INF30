import java.util.List;
import java.util.ArrayList;

class Empleado extends Usuario {
    private int empleadoID;
    private String correoInstitucional;
    private String numeroCelular;
    private Rol rol;
    private Area area;
    private List<CuentaBancaria> cuentas; //inicializado

    private List<SolicitudGasto> solicitudes; //inicializado


	public Empleado(int usuarioID, String nombre, String apellido_paterno,
					String apellido_materno, String password,EstadoUsuario estado,
						int empleadoID, String correoInstitucional, String numeroCelular){
		super(usuarioID, nombre,  apellido_paterno,
				 apellido_materno,  password, estado);
		this.empleadoID=empleadoID;
		this.correoInstitucional=correoInstitucional;
		this.numeroCelular=numeroCelular;
		this.cuentas= new ArrayList<>();
		this.solicitudes= new ArrayList<>();
	}  

	public void setRol(Rol rol){
		this.rol=rol;
	}  
	public Rol getRol(){
		Rol rolito= new Rol(rol);
		return rolito;
	}
    // Getters
    public int getEmpleadoID() {
        return empleadoID;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }

    // Setters
    public void setEmpleadoID(int empleadoID) {
        this.empleadoID = empleadoID;
    }

    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }

    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
    }
}
