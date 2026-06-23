package pe.edu.pucp.economix.operaciones.model;

import java.util.Date;

import pe.edu.pucp.economix.operaciones.model.enums.EstadoPermisoEdicion;
import pe.edu.pucp.economix.rrhh.model.Empleado;

public class PermisoEdicion {
    private int idPermiso;
    private Date fechaCreacion;
    private Date fechaExpiracion;
    private Date fechaUso;
    private String motivoSolicitud;
    private String motivoAutorizacion;
    private EstadoPermisoEdicion estado;

    // Relaciones
    private Empleado solicitante;
    private Empleado autorizador;
    private ComprobantePago comprobante;

    public PermisoEdicion() {}

    public PermisoEdicion(int idPermiso, Date fechaCreacion, Date fechaExpiracion, Date fechaUso,
                          String motivoSolicitud, String motivoAutorizacion, EstadoPermisoEdicion estado,
                          Empleado solicitante, Empleado autorizador, ComprobantePago comprobante) {
        this.idPermiso = idPermiso;
        this.fechaCreacion = fechaCreacion;
        this.fechaExpiracion = fechaExpiracion;
        this.fechaUso = fechaUso;
        this.motivoSolicitud = motivoSolicitud;
        this.motivoAutorizacion = motivoAutorizacion;
        this.estado = estado;
        this.solicitante = solicitante;
        this.autorizador = autorizador;
        this.comprobante = comprobante;
    }

    public PermisoEdicion(Date fechaCreacion, Date fechaExpiracion, Date fechaUso,
                          String motivoSolicitud, String motivoAutorizacion, EstadoPermisoEdicion estado,
                          Empleado solicitante, Empleado autorizador, ComprobantePago comprobante) {
        this.fechaCreacion = fechaCreacion;
        this.fechaExpiracion = fechaExpiracion;
        this.fechaUso = fechaUso;
        this.motivoSolicitud = motivoSolicitud;
        this.motivoAutorizacion = motivoAutorizacion;
        this.estado = estado;
        this.solicitante = solicitante;
        this.autorizador = autorizador;
        this.comprobante = comprobante;
    }

    public int getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(int idPermiso) {
        this.idPermiso = idPermiso;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public Date getFechaUso() {
        return fechaUso;
    }

    public void setFechaUso(Date fechaUso) {
        this.fechaUso = fechaUso;
    }

    public String getMotivoSolicitud() {
        return motivoSolicitud;
    }

    public void setMotivoSolicitud(String motivoSolicitud) {
        this.motivoSolicitud = motivoSolicitud;
    }

    public String getMotivoAutorizacion() {
        return motivoAutorizacion;
    }

    public void setMotivoAutorizacion(String motivoAutorizacion) {
        this.motivoAutorizacion = motivoAutorizacion;
    }

    public EstadoPermisoEdicion getEstado() {
        return estado;
    }

    public void setEstado(EstadoPermisoEdicion estado) {
        this.estado = estado;
    }

    public Empleado getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Empleado solicitante) {
        this.solicitante = solicitante;
    }

    public Empleado getAutorizador() {
        return autorizador;
    }

    public void setAutorizador(Empleado autorizador) {
        this.autorizador = autorizador;
    }

    public ComprobantePago getComprobante() {
        return comprobante;
    }

    public void setComprobante(ComprobantePago comprobante) {
        this.comprobante = comprobante;
    }

    @Override
    public String toString() {
        return "PermisoEdicion{" +
                "idPermiso=" + idPermiso +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaExpiracion=" + fechaExpiracion +
                ", fechaUso=" + fechaUso +
                ", motivoSolicitud='" + motivoSolicitud + '\'' +
                ", motivoAutorizacion='" + motivoAutorizacion + '\'' +
                ", estado=" + estado +
                ", solicitante=" + solicitante +
                ", autorizador=" + autorizador +
                ", comprobante=" + comprobante +
                '}';
    }
}
