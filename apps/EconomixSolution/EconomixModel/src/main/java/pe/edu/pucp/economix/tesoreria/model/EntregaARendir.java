package pe.edu.pucp.economix.tesoreria.model;

import java.util.Date;

public class EntregaARendir extends Fondo{
    private String motivo;
    private double montoSolicitado;
    private Date fechaSolicitud;
    private Date fechaAperturaEntrega;
    private Date fechaCierreEntrega;
    private EstadoEntregaARendir estado;

    //Constructores
    public EntregaARendir( String nombre, double saldoActual, EstadoFondo estadoFondo, Date fechaCreacion,
                           String motivo, double montoSolicitado, Date fechaSolicitud, Date fechaAperturaEntrega, Date fechaCierreEntrega,
                           EstadoEntregaARendir estado) {
        super(nombre,saldoActual,estadoFondo,fechaCreacion);
        this.motivo = motivo;
        this.montoSolicitado = montoSolicitado;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaAperturaEntrega = fechaAperturaEntrega;
        this.fechaCierreEntrega = fechaCierreEntrega;
        this.estado = estado;
    }

    //Selectores
    public String getMotivo() {
        return motivo;
    }
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    public double getMontoSolicitado() {
        return montoSolicitado;
    }
    public void setMontoSolicitado(double montoSolicitado) {
        this.montoSolicitado = montoSolicitado;
    }
    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }
    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }
    public Date getFechaAperturaEntrega() {
        return fechaAperturaEntrega;
    }
    public void setFechaAperturaEntrega(Date fechaAperturaEntrega) {
        this.fechaAperturaEntrega = fechaAperturaEntrega;
    }
    public Date getFechaCierreEntrega() {
        return fechaCierreEntrega;
    }
    public void setFechaCierreEntrega(Date fechaCierreEntrega) {
        this.fechaCierreEntrega = fechaCierreEntrega;
    }
    public EstadoEntregaARendir getEstadoEntrega() {
        return estado;
    }
    public void setEstado(EstadoEntregaARendir estado) {
        this.estado = estado;
    }

    //Metodos
    public void aperturarEntrega(double montoAprobado) {
        // TODO: Registrar fechaApertura y cambiar estado a "Activo" (RF_06)
    }

    public void cerrarEntrega() {
        // TODO: Bloquear nuevos gastos y proceder a la liquidación final (RF_10)
    }

    public boolean verificarPlazoVencido() {
        // TODO: Validar si la fecha actual supera la fechaCierreEntrega
        return false;
    }
}