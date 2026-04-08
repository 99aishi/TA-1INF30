abstract class Usuario{
	private int usuarioID;
	private String nombre;
	private String apellido_paterno;
	private String apellido_materno;
	private String password; //encriptado (hash?)
	private EstadoUsuario estado;
	
	public Usuario(int usuarioID, String nombre, String apellido_paterno,
	 String apellido_materno, String password,EstadoUsuario estado){
		this.usuarioID=usuarioID;
		this.nombre=nombre;
		this.password=password;
		this.apellido_paterno=apellido_paterno;
		this.apellido_materno=apellido_materno;
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
	public String getPassword(){
		return password;
	}
	public void setPassword(String password){
		this.password=password;
	}
	
	public void cambiarPassword(String nueva){
		this.password=nueva;
	}

	public boolean login(int idIngresado, String passIngresada){
		if(usuarioID==idIngresado)
			if(estado==EstadoUsuario.Activo){
				//return verificarPassword(passIngresada,this.password)
				if(passIngresada==this.password) return true;
			}
		return false;
	}





}