package pe.edu.pucp.economix.operaciones.model;

import java.util.Date;
import java.util.List;

import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;

public class SolicitudGasto{
    private int idSolicitudGasto;
    private Date fechaSolicitud;
    private double montoSolicitado;
    private String motivoSolicitud;
    private EstadoSolicitudGasto estado;
    //Relaciones
    private Empleado solicitante;
    private Empleado destinatario;
    private List<ComprobantePago> comprobantes;
    private CicloCajaChica ciclo;

    //Constructores
    public SolicitudGasto(){}

    public SolicitudGasto(int idSolicitudGasto, Date fechaSolicitud, double montoSolicitado, String motivoSolicitud,
                          EstadoSolicitudGasto estado, Empleado solicitante, Empleado destinatario,
                          List<ComprobantePago> comprobantes, CicloCajaChica ciclo) {
        this.idSolicitudGasto = idSolicitudGasto;
        this.fechaSolicitud = fechaSolicitud;
        this.montoSolicitado = montoSolicitado;
        this.motivoSolicitud = motivoSolicitud;
        this.estado = estado;
        this.solicitante = solicitante;
        this.destinatario = destinatario;
        this.comprobantes = comprobantes;
        this.ciclo = ciclo;
    }

    //Selectores
    public int getIdSolicitudGasto() {
        return idSolicitudGasto;
    }
    public void setIdSolicitudGasto(int idSolicitudGasto) {
        this.idSolicitudGasto = idSolicitudGasto;
    }
    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }
    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }
    public double getMontoSolicitado() {
        return montoSolicitado;
    }
    public void setMontoSolicitado(double montoSolicitado) {
        this.montoSolicitado = montoSolicitado;
    }
    public String getMotivoSolicitud() {
        return motivoSolicitud;
    }
    public void setMotivoSolicitud(String motivoSolicitud) {
        this.motivoSolicitud = motivoSolicitud;
    }
    public EstadoSolicitudGasto getEstado() {
        return estado;
    }
    public void setEstado(EstadoSolicitudGasto estado) {
        this.estado = estado;
    }
    public Empleado getSolicitante() {
        return solicitante;
    }
    public void setSolicitante(Empleado solicitante) {
        this.solicitante = solicitante;
    }
    public Empleado getDestinatario() {
        return destinatario;
    }
    public void setDestinatario(Empleado destinatario) {
        this.destinatario = destinatario;
    }
    public CicloCajaChica getCiclo() {
        return ciclo;
    }
    public void setCiclo(CicloCajaChica ciclo) {
        this.ciclo = ciclo;
    }
    public List<ComprobantePago> getComprobantes() {
        return comprobantes;
    }
    public void setComprobantes(List<ComprobantePago> comprobantes) {
        this.comprobantes = comprobantes;
    }

    //Metodos


    @Override
    public String toString() {
        return "SolicitudGasto{" +
                "idSolicitudGasto=" + idSolicitudGasto +
                ", fechaSolicitud=" + fechaSolicitud +
                ", montoSolicitado=" + montoSolicitado +
                ", motivoSolicitud='" + motivoSolicitud + '\'' +
                ", estado=" + estado +
                ", solicitante=" + solicitante +
                ", destinatario=" + destinatario +
                ", comprobantes=" + comprobantes +
                ", ciclo=" + ciclo +
                '}';
    }

    public void evaluarSolicitud(Empleado jefe, boolean aprobado, String comentario) {
        // TODO: Registro obligatorio de fecha y sustento (RF_07)
    }

    public void registrarDesembolso(String nroOperacion, CuentaBancaria destino) {
        // TODO: Cambiar estado a "desembolsado" y vincular transacción (RF_08)
    }

}
