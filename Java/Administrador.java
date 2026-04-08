class Administrador extends Usuario{
    private static int correlativoID = 1;
    private int adminID;
	private String correoSoporte;

    //Constructores
	public Administrador(String nombre, String apellido_paterno,
					String apellido_materno, String password, EstadoUsuario estado,
                     String correoSoporte){
		super(nombre,  apellido_paterno, apellido_materno,  password, estado);

		this.adminID=this.correlativoID++;
		this.correoSoporte=correoSoporte;
	}


    //Selectores
    public int getAdminID() {
        return adminID;
    }

    public String getCorreoSoporte() {
        return correoSoporte;
    }
    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public void setCorreoSoporte(String correoSoporte) {
        this.correoSoporte = correoSoporte;
    }

	//crearUsuario(int usuarioID, String nombre, String apellido_paterno,
	//String apellido_materno, String password,EstadoUsuario estado)

	//asignarRol(usuario,rol)
	//cambiarEstadoUsuario


}