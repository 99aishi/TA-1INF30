import java.util.Date;

class Fondo {
    private int idFondo;
    private String nombre;
    private double saldoActual;
    private EstadoFondo estado;
    private Date fechaCreacion;

    // Constructor
    public Fondo(int idFondo, String nombre, double saldoActual, EstadoFondo estado, Date fechaCreacion) {
        this.idFondo = idFondo;
        this.nombre = nombre;
        this.saldoActual = saldoActual;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters
    public int getIdFondo() {
        return idFondo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getSaldoActual() {
        return saldoActual;
    }

    public EstadoFondo getEstado() {
        return estado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    // Setters
    public void setIdFondo(int idFondo) {
        this.idFondo = idFondo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setSaldoActual(double saldoActual) {
        this.saldoActual = saldoActual;
    }

    public void setEstado(EstadoFondo estado) {
        this.estado = estado;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
