package pe.edu.pucp.economix.operaciones.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Rendicion {
    private int idRendicion;
    private Date fechaPresentacion;
    private Date fechaAprobacion;
    private double totalDeclarado;
    private double totalAprobado;
    private double saldoFinal;
    private EstadoRendicion estado;
    private String comentario;
    //Relaciones
    CicloCajaChica cicloCajaChica;

    // Constructores
    public Rendicion(){}
    public Rendicion(int idRendicion, Date fechaPresentacion, Date fechaAprobacion,
                     double totalDeclarado, double totalAprobado, double saldoFinal,
                     EstadoRendicion estado, String comentario, CicloCajaChica cicloCajaChica) {
        this.idRendicion = idRendicion;
        this.fechaPresentacion = fechaPresentacion;
        this.fechaAprobacion = fechaAprobacion;
        this.totalDeclarado = totalDeclarado;
        this.totalAprobado = totalAprobado;
        this.saldoFinal = saldoFinal;
        this.estado = estado;
        this.comentario = comentario;
        this.cicloCajaChica = cicloCajaChica;
    }

    public Rendicion(Date fechaPresentacion, Date fechaAprobacion,
                     double totalDeclarado, double totalAprobado, double saldoFinal,
                     EstadoRendicion estado, String comentario, CicloCajaChica cicloCajaChica) {
        this.fechaPresentacion = fechaPresentacion;
        this.fechaAprobacion = fechaAprobacion;
        this.totalDeclarado = totalDeclarado;
        this.totalAprobado = totalAprobado;
        this.saldoFinal = saldoFinal;
        this.estado = estado;
        this.comentario = comentario;
        this.cicloCajaChica = cicloCajaChica;
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
    public void setEstado(EstadoRendicion nuevoEstado) {
        // Permitir la asignación inicial si el estado es nulo
        if (this.estado == null) {
            this.estado = nuevoEstado;
            return;
        }

        // Validación de transición controlada
        if (this.estado.puedeTransicionarA(nuevoEstado)) {
            this.estado = nuevoEstado;
        } else {
            // Log de error o manejo de excepción según la arquitectura del proyecto
            System.err.println("Error: No se permite cambiar el estado de "
                    + this.estado + " a " + nuevoEstado);
        }
    }
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    public CicloCajaChica getCicloCajaChica() {
        return cicloCajaChica;
    }
    public void setCicloCajaChica(CicloCajaChica cicloCajaChica) {
        this.cicloCajaChica = cicloCajaChica;
    }

    //Metodos

    @Override
    public String toString() {
        return "Rendicion{" +
                "idRendicion=" + idRendicion +
                ", fechaPresentacion=" + fechaPresentacion +
                ", fechaAprobacion=" + fechaAprobacion +
                ", totalDeclarado=" + totalDeclarado +
                ", totalAprobado=" + totalAprobado +
                ", saldoFinal=" + saldoFinal +
                ", estado=" + estado +
                ", comentario='" + comentario + '\'' +
                ", cicloCajaChica=" + cicloCajaChica +
                '}';
    }


    public void calcularTotalAprobado(){
        this.totalAprobado=this.cicloCajaChica.getTotalGastado();
    }
    public void calcularTotalDeclarado(){
        double declarado=0;
        for(SolicitudGasto s :this.cicloCajaChica.getSolicitudesDeGasto()){
            declarado += s.sumaComprobantes();
        }
        this.totalDeclarado=declarado;
    }

}
