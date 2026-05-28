package pe.edu.pucp.economix.operaciones.model;

import java.util.Date;
import java.util.List;

import pe.edu.pucp.economix.operaciones.model.enums.EstadoCicloCaja;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoSolicitudGasto;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;

public class CicloCajaChica{
    private int idCicloCaja;
    private int numeroSemana;
    private Date fechaApertura;
    private Date fechaCierre;
    private double saldoInicial;
    private double totalGastado;
    private EstadoCicloCaja estado; // este enum es solo activo/inactivo no veo necesario hacer un enum para esto. puede ser un boolean

    //Relaciones
    private CajaChica cajaChica;
    private List<SolicitudGasto> solicitudesDeGasto;
    private Rendicion rendicion;

    //Constructores
    public CicloCajaChica(){}
    public CicloCajaChica(int idCicloCaja, int numeroSemana, Date fechaApertura, Date fechaCierre,
                          double saldoInicial, double totalGastado, EstadoCicloCaja estado,
                          CajaChica cajaChica, List<SolicitudGasto> solicitudesDeGasto, Rendicion rendicion) {
        this.idCicloCaja = idCicloCaja;
        this.numeroSemana = numeroSemana;
        this.fechaApertura = fechaApertura;
        this.fechaCierre = fechaCierre;
        this.saldoInicial = saldoInicial;
        this.totalGastado = totalGastado;
        this.estado = estado;
        this.cajaChica = cajaChica;
        this.solicitudesDeGasto = solicitudesDeGasto;
        this.rendicion = rendicion;
    }
    public CicloCajaChica(int numeroSemana, Date fechaApertura, Date fechaCierre, double saldoInicial,
                          double totalGastado, EstadoCicloCaja estado, CajaChica cajaChica,
                          List<SolicitudGasto> solicitudesDeGasto, Rendicion rendicion) {
        this.numeroSemana = numeroSemana;
        this.fechaApertura = fechaApertura;
        this.fechaCierre = fechaCierre;
        this.saldoInicial = saldoInicial;
        this.totalGastado = totalGastado;
        this.estado = estado;
        this.cajaChica = cajaChica;
        this.solicitudesDeGasto = solicitudesDeGasto;
        this.rendicion = rendicion;
    }

    //Selectores
    public int getIdCicloCaja() {
        return idCicloCaja;
    }
    public void setIdCicloCaja(int idCicloCaja) {
        this.idCicloCaja = idCicloCaja;
    }
    public int getNumeroSemana() {
        return numeroSemana;
    }
    public void setNumeroSemana(int numeroSemana) {
        this.numeroSemana = numeroSemana;
    }
    public Date getFechaApertura() {
        return fechaApertura;
    }
    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }
    public Date getFechaCierre() {
        return fechaCierre;
    }
    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }
    public double getSaldoInicial() {
        return saldoInicial;
    }
    public void setSaldoInicial(double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }
    public double getTotalGastado() {
        return totalGastado;
    }
    public void setTotalGastado(double totalGastado) {
        this.totalGastado = totalGastado;
    }
    public EstadoCicloCaja getEstado() {
        return estado;
    }
    public void setEstado(EstadoCicloCaja estado) {
        this.estado = estado;
    }
    public CajaChica getCajaChica(){
        return this.cajaChica;
    }
    public void setCajaChica(CajaChica cajaChica){
        this.cajaChica = cajaChica;
    }
    public List<SolicitudGasto> getSolicitudesDeGasto() {
        return solicitudesDeGasto;
    }
    public void setSolicitudesDeGasto(List<SolicitudGasto> solicitudesDeGasto) {
        this.solicitudesDeGasto = solicitudesDeGasto;
    }
    public Rendicion getRendicion() {
        return rendicion;
    }
    public void setRendicion(Rendicion rendicion) {
        this.rendicion = rendicion;
    }

    @Override
    public String toString() {
        return "CicloCajaChica{" +
                "idCicloCaja=" + idCicloCaja +
                ", numeroSemana=" + numeroSemana +
                ", fechaApertura=" + fechaApertura +
                ", fechaCierre=" + fechaCierre +
                ", saldoInicial=" + saldoInicial +
                ", totalGastado=" + totalGastado +
                ", estado=" + estado +
                ", cajaChica=" + cajaChica +
                ", solicitudesDeGasto=" + solicitudesDeGasto +
                ", rendicion=" + rendicion +
                '}';
    }

    public void calcularTotalGastado(){
        double total=0;
        for (SolicitudGasto s: solicitudesDeGasto){
            if(s.getEstado()== EstadoSolicitudGasto.Aprobado){
                total+=s.getMontoSolicitado();
            }
        }
        setTotalGastado(total);
    }

}
