import java.util.Date;

class Rendicion {
    private static int correlativoID = 1;
    private int idRendicion;
    private Date fechaPresentacion;
    private Date fechaAprobacion;
    private double totalDeclarado;
    private double totalAprobado;
    private double saldoFinal;
    private EstadoRendicion estado;
    private String comentario;
    
    // Constructores
    public Rendicion(Date fechaPresentacion, Date fechaAprobacion,
                     double totalDeclarado, double totalAprobado, double saldoFinal,
                     EstadoRendicion estado, String comentario) {
        this.idRendicion = this.correlativoID++;
        this.fechaPresentacion = fechaPresentacion;
        this.fechaAprobacion = fechaAprobacion;
        this.totalDeclarado = totalDeclarado;
        this.totalAprobado = totalAprobado;
        this.saldoFinal = saldoFinal;
        this.estado = estado;
        this.comentario = comentario;
    }

    //Selectores
    public int getIdRendicion() {
        return idRendicion;
    }
    public Date getFechaPresentacion() {
        return fechaPresentacion;
    }
    public Date getFechaAprobacion() {
        return fechaAprobacion;
    }
    public double getTotalDeclarado() {
        return totalDeclarado;
    }
    public double getTotalAprobado() {
        return totalAprobado;
    }
    public double getSaldoFinal() {
        return saldoFinal;
    }
    public EstadoRendicion getEstado() {
        return estado;
    }
    public String getComentario() {
        return comentario;
    }
    public void setIdRendicion(int idRendicion) {
        this.idRendicion = idRendicion;
    }
    public void setFechaPresentacion(Date fechaPresentacion) {
        this.fechaPresentacion = fechaPresentacion;
    }
    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }
    public void setTotalDeclarado(double totalDeclarado) {
        this.totalDeclarado = totalDeclarado;
    }
    public void setTotalAprobado(double totalAprobado) {
        this.totalAprobado = totalAprobado;
    }
    public void setSaldoFinal(double saldoFinal) {
        this.saldoFinal = saldoFinal;
    }
    public void setEstado(EstadoRendicion estado) {
        this.estado = estado;
    }
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    //Metodos
}
