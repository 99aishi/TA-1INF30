package pe.edu.pucp.economix.operaciones.model;

import pe.edu.pucp.economix.tesoreria.model.CajaChica;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CicloCajaChica{
    private static int correlativoID = 1;
    private int idCicloCaja;
    private int numeroSemana;
    private Date fechaApertura;
    private Date fechaCierre;
    private double saldoInicial;
    private double totalGastado;
    private EstadoCicloCaja estado;
    private CajaChica cajaChica;
    private List<SolicitudGasto> solicitudes;

    //Constructores
    public CicloCajaChica(int numeroSemana,Date fechaApertura,Date fechaCierre, double saldoInicial, CajaChica cajaChica){
        this.idCicloCaja=this.correlativoID++;
        this.numeroSemana=numeroSemana;
        this.fechaApertura=fechaApertura;
        this.fechaCierre=fechaCierre;
        this.saldoInicial=saldoInicial;
        this.totalGastado=0;
        this.estado=EstadoCicloCaja.Activo;
        this.cajaChica = cajaChica;
        this.solicitudes=new ArrayList<>();
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

    //Metodos
    public void abrirCiclo() {
        // TODO: Heredar saldo anterior y congelar monto techo (RF_12)
    }

    public double cerrarYConsolidarCiclo() {
        // TODO: Inhabilitar nuevos registros y calcular total rendido (RF_16)
        return 0.0;
    }
}