package pe.edu.pucp.economix.rrhh.model;

public class Rol {
    private int rolID;
    private String titulo;
    private String descripcion;
    private boolean estaActivo;
    private Area area;
    // Constructor
    public Rol(){}

    public Rol(int rolID, String titulo, String descripcion, Area area) {
        this.rolID = rolID;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.area = area;
        this.estaActivo = true;
    }
    public Rol(String titulo, String descripcion, Area area) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.area = area;
        this.estaActivo = true;
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
    public boolean isEstaActivo() {
        return estaActivo;
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
    public void setEstaActivo(boolean estaActivo) {
        this.estaActivo = estaActivo;
    }
    public Area getArea() {
        return area;
    }
    public void setArea(Area area) {
        this.area = area;
    }

    //Metodos

    @Override
    public String toString() {
        return "Rol{" +
                "rolID=" + rolID +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", area=" + area +
                '}';
    }
}