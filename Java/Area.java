import java.util.ArrayList;
import java.util.List;

class Area{
	private static int correlativoID = 1;
	private int idArea;
	private String nombre;
	private String descripcion;
	private List<Empleado> empleados;

	//Constructores
	public Area(String nombre, String descripcion) {

		this.idArea = this.correlativoID++;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.empleados=new ArrayList<>();
	}	

	//Selectores
	public int getIdArea() {
		return idArea;
	}
	public void setIdArea(int idArea) {
		this.idArea = idArea;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	//Metodos
	
}