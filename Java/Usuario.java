abstract class Usuario{
	private static int correlativoID = 1;
	private int usuarioID;
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String password;
	private EstadoUsuario estado;
	
	//Constructores
	public Usuario(String nombre, String apellido_paterno,
	 	String apellido_materno, String password,EstadoUsuario estado){
		//Asignación de ID automatica por el sistema
		this.usuarioID = this.correlativoID++;
		this.nombre=nombre;
		this.password=password;
		this.apellidoPaterno=apellido_paterno;
		this.apellidoMaterno=apellido_materno;
		this.estado=estado;
	}

	//Selectores
	public int getUsuarioID(){
		return usuarioID;
	}
	public void setUsuarioID(int usuarioID){
		this.usuarioID=usuarioID;
	}
	public String getNombre(){
		return nombre;
	}
	public void setNombre(String nombre){
		this.nombre=nombre;
	}
	public String getApellido_paterno(){
		return apellidoPaterno;
	}
	public void setApellido_paterno(String apellido_paterno){
		this.apellidoPaterno=apellido_paterno;
	}
	public String getApellido_materno(){
		return apellidoMaterno;
	}
	public void setApellido_materno(String apellido_materno){
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

	//Metodos
	public boolean login(int idIngresado, String passIngresada){
		if(usuarioID == idIngresado)
			if(estado == EstadoUsuario.Activo){
				//return verificarPassword(passIngresada,this.password)
				
				if(passIngresada.equals(this.password)) return true;
			}
		return false;
	}
		public String cifrarPassword(String passwordPlano) {
		// TODO: Implementar algoritmo Argon2id con salt de 16 bytes (RNF_01)
		return "";
	}

	public boolean validarAccesoPorEstado() {
		// TODO: Restringir acceso exclusivamente a "Activos" (RF_05)
		return false;
	}
}