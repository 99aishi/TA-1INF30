class Administrador extends Usuario{
	private int adminID;
	private String correoSoporte;

	public Administrador(int usuarioID, String nombre, String apellido_paterno,
					String apellido_materno, String password,EstadoUsuario estado,
						int adminID, String correoSoporte){
		super(usuarioID, nombre,  apellido_paterno,
				 apellido_materno,  password, estado);
		this.adminID=adminID;
		this.correoSoporte=correoSoporte;
	}


    // Getters
    public int getAdminID() {
        return adminID;
    }

    public String getCorreoSoporte() {
        return correoSoporte;
    }

    // Setters
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