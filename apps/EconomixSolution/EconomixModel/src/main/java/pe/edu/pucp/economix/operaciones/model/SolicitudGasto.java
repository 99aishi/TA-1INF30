package pe.edu.pucp.economix.operaciones.model;

import java.util.Date;
import java.util.List;

import pe.edu.pucp.economix.operaciones.model.enums.EstadoSolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.enums.MedioPago;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

public class SolicitudGasto{
    private int idSolicitudGasto;
    private Date fechaSolicitud;
    private double montoSolicitado;
    private Moneda monedaOriginal;
    private double tipoCambio;
    private double montoConvertido;
    private String motivoSolicitud;
    private EstadoSolicitudGasto estado;
    private MedioPago medioDesembolso;
    private String comentarioDecision;
    //Relaciones
    private Empleado solicitante;
    private Empleado destinatario;
    private Empleado jefeAprobador;
    private Empleado tesoreroAprobador;
    private List<ComprobantePago> comprobantes;
    private CicloCajaChica ciclo;

    //Constructores
    public SolicitudGasto(){}
    public SolicitudGasto(int idSolicitudGasto, Date fechaSolicitud, double montoSolicitado, String motivoSolicitud,
                          EstadoSolicitudGasto estado, String comentarioDecision, Empleado solicitante, Empleado destinatario,
                          List<ComprobantePago> comprobantes, CicloCajaChica ciclo) {
        this.idSolicitudGasto = idSolicitudGasto;
        this.fechaSolicitud = fechaSolicitud;
        this.montoSolicitado = montoSolicitado;
        this.motivoSolicitud = motivoSolicitud;
        this.estado = estado;
        this.comentarioDecision = comentarioDecision;
        this.solicitante = solicitante;
        this.destinatario = destinatario;
        this.comprobantes = comprobantes;
        this.ciclo = ciclo;
    }

    public SolicitudGasto(Date fechaSolicitud, double montoSolicitado, String motivoSolicitud,
                          EstadoSolicitudGasto estado, String comentarioDecision, Empleado solicitante, Empleado destinatario,
                          List<ComprobantePago> comprobantes, CicloCajaChica ciclo) {
        this.fechaSolicitud = fechaSolicitud;
        this.montoSolicitado = montoSolicitado;
        this.motivoSolicitud = motivoSolicitud;
        this.estado = estado;
        this.comentarioDecision = comentarioDecision;
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
    public Moneda getMonedaOriginal() {
        return monedaOriginal;
    }
    public void setMonedaOriginal(Moneda monedaOriginal) {
        this.monedaOriginal = monedaOriginal;
    }
    public double getTipoCambio() {
        return tipoCambio;
    }
    public void setTipoCambio(double tipoCambio) {
        this.tipoCambio = tipoCambio;
    }
    public double getMontoConvertido() {
        return montoConvertido;
    }
    public void setMontoConvertido(double montoConvertido) {
        this.montoConvertido = montoConvertido;
    }
    public EstadoSolicitudGasto getEstado() {
        return estado;
    }
    public MedioPago getMedioDesembolso() {
        return medioDesembolso;
    }
    public void setMedioDesembolso(MedioPago medioDesembolso) {
        this.medioDesembolso = medioDesembolso;
    }

    public void setEstado(EstadoSolicitudGasto nuevoEstado) {
        this.estado = nuevoEstado;
    }
    public String getComentarioDecision(){
        return comentarioDecision;
    }
    public void setComentarioDecision(String comentarioDecision){
        this.comentarioDecision=comentarioDecision;
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
    public Empleado getJefeAprobador() {
        return jefeAprobador;
    }
    public void setJefeAprobador(Empleado jefeAprobador) {
        this.jefeAprobador = jefeAprobador;
    }
    public Empleado getTesoreroAprobador() {
        return tesoreroAprobador;
    }
    public void setTesoreroAprobador(Empleado tesoreroAprobador) {
        this.tesoreroAprobador = tesoreroAprobador;
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


    @Override
    public String toString() {
        return "SolicitudGasto{" +
                "idSolicitudGasto=" + idSolicitudGasto +
                ", fechaSolicitud=" + fechaSolicitud +
                ", montoSolicitado=" + montoSolicitado +
                ", monedaOriginal=" + monedaOriginal +
                ", tipoCambio=" + tipoCambio +
                ", montoConvertido=" + montoConvertido +
                ", motivoSolicitud='" + motivoSolicitud + '\'' +
                ", estado=" + estado +
                ", medioDesembolso=" + medioDesembolso +
                ", comentarioDecision='" + comentarioDecision + '\'' +
                ", solicitante=" + solicitante +
                ", destinatario=" + destinatario +
                ", jefeAprobador=" + jefeAprobador +
                ", tesoreroAprobador=" + tesoreroAprobador +
                ", comprobantes=" + comprobantes +
                ", ciclo=" + ciclo +
                '}';
    }

    public void evaluarSolicitud(Empleado jefe, boolean aprobado, String comentario) {
        if (jefe == null) {
            throw new IllegalArgumentException("El jefe evaluador no puede ser nulo");
        }
        this.comentarioDecision = comentario;
        this.jefeAprobador = jefe;
        if (aprobado) {
            this.estado = EstadoSolicitudGasto.APROBADO;
        } else {
            this.estado = EstadoSolicitudGasto.RECHAZADO;
        }
    }
    public double sumaComprobantes(){
        double suma=0;
        for (ComprobantePago c : comprobantes){
            suma+=c.getTotal();
        }
        return suma;
    }
    public void registrarDesembolso(String nroOperacion, CuentaBancaria destino) {
        if (nroOperacion == null || nroOperacion.isEmpty()) {
            throw new IllegalArgumentException("El número de operación es obligatorio");
        }
        this.estado = EstadoSolicitudGasto.PAGADO;
    }

}
