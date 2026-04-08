import java.util.List;
import java.util.ArrayList;

class Area{
	private int idArea;
	private String nombre;
	private String descripcion;
	private List<Empleado> empleados;
	public Area(int idArea, String nombre, String descripcion) {
		this.idArea = idArea;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.empleados=new ArrayList<>();
	}	

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
}