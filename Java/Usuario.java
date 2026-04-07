abstract class Usuario{
	private int usuarioID;
	private String nombre;
	private String apellido_paterno;
	private String apellido_materno;
	private String correo;
	private EstadoUsuario estado;
	
	public Usuario(int usuarioID, String nombre, String apellido_paterno,
	 String apellido_materno, String correo,EstadoUsuario estado){
		this.usuarioID=usuarioID;
		this.nombre=nombre;
		this.apellido_paterno=apellido_paterno;
		this.apellido_materno=apellido_materno;
		this.correo=correo;
		this.estado=estado;
	}

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
		return apellido_paterno;
	}
	public void setApellido_paterno(String apellido_paterno){
		this.apellido_paterno=apellido_paterno;
	}
	public String getApellido_materno(){
		return apellido_materno;
	}
	public void setApellido_materno(String apellido_materno){
		this.apellido_materno=apellido_materno;
	}
	public String getCorreo(){
		return correo;
	}
	public void setCorreo(String correo){
		this.correo=correo;
	}
	







}