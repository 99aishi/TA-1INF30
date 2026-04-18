package pe.edu.pucp.economix.rrhh.model;

class Rol {
    private static int correlativoID = 1;
    private int rolID;
    private String titulo;
    private String descripcion;
    // Constructor
    public Rol(String titulo, String descripcion) {
        this.rolID = this.correlativoID++;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }
    public Rol(Rol copia){
        this.rolID= copia.rolID;
        this.titulo=copia.titulo;
        this.descripcion=copia.descripcion;
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
}