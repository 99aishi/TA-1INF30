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