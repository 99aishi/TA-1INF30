package pe.edu.pucp.economix.rrhh.model;

public class Rol {
    private int rolID;
    private String titulo;
    private String descripcion;
    // Constructor
    public Rol(){

    }
    public Rol(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    //Selectores
    public int getRolID() {
        return rolID;
    }
    public String getTitulo() {
        return titulo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setRolID(int rolID) {
        this.rolID = rolID;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    //Metodos

    @Override
    public String toString() {
        return "Rol{" +
                "rolID=" + rolID +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}