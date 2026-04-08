import java.util.List;
import java.util.ArrayList;


class Rol {
    private int rolID;
    private String titulo;
    private String descripcion;
    // Constructor
    public Rol(int rolID, String titulo, String descripcion) {
        this.rolID = rolID;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public Rol(Rol copia){
    	this.rolID= copia.rolID;
    	this.titulo=copia.titulo;
    	this.descripcion=copia.descripcion;
    }

    // Getters
    public int getRolID() {
        return rolID;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    // Setters
    public void setRolID(int rolID) {
        this.rolID = rolID;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
