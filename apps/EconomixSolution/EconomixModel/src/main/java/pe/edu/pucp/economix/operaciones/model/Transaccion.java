package pe.edu.pucp.economix.operaciones.model;

import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.util.Date;

public class Transaccion{
    private static int correlativoID = 1;
    private int idTransaccion;
    private TipoTransaccion tipoTransaccion;
    private Date fecha;
    private double monto;
    private String numeroOperacionBancaria;
    private MedioPago medioPago;
    private double tipoCambio;
    private CuentaBancaria cuentaOrigen;
    private CuentaBancaria cuentaDestino;
    private Moneda moneda;

    public Transaccion(TipoTransaccion tipo,Date fecha,double monto,
                       String numeroOperacionBancaria, MedioPago medioPago, double tipoCambio,
                       CuentaBancaria origen, CuentaBancaria destino, Moneda moneda){
        this.idTransaccion=this.correlativoID++;
        this.tipoTransaccion = tipo;
        this.fecha=fecha;
        this.monto=monto;
        this.numeroOperacionBancaria=numeroOperacionBancaria;
        this.medioPago=medioPago;
        this.tipoCambio=tipoCambio;
        this.cuentaOrigen = origen;
        this.cuentaDestino = destino;
        this.moneda = moneda;
    }

    // Selectores
    public int getIdTransaccion() {
        return idTransaccion;
    }
    public TipoTransaccion getTipoTransaccion() {
        return tipoTransaccion;
    }
    public Date getFecha() {
        return fecha;
    }
    public double getMonto() {
        return monto;
    }
    public String getNumeroOperacionBancaria() {
        return numeroOperacionBancaria;
    }
    public MedioPago getMedioPago() {
        return medioPago;
    }
    public double getTipoCambio() {
        return tipoCambio;
    }
    public CuentaBancaria getCuentaOrigen(){
        return this.cuentaOrigen;
    }
    public CuentaBancaria getCuentaDestino(){
        return this.cuentaDestino;
    }
    public Moneda getMoneda(){
        return this.moneda;
    }
    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }
    public void setTipoTransaccion(TipoTransaccion tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public void setMonto(double monto) {
        this.monto = monto;
    }
    public void setNumeroOperacionBancaria(String numeroOperacionBancaria) {
        this.numeroOperacionBancaria = numeroOperacionBancaria;
    }
    public void setMedioPago(MedioPago medioPago) {
        this.medioPago = medioPago;
    }
    public void setTipoCambio(double tipoCambio) {
        this.tipoCambio = tipoCambio;
    }
    public void setCuentaOrigen(CuentaBancaria cuenta){
        this.cuentaOrigen = cuenta;
    }
    public void setCuentaDestino(CuentaBancaria cuenta){
        this.cuentaDestino = cuenta;
    }
    public void setMoneda(Moneda moneda){
        this.moneda = moneda;
    }

    //Metodos
    public void registrarLogTrazabilidad() {
        // TODO: Registro inmutable con precisión de milisegundos (RF_19, RNF_07)
    }
    public void generarReporteGastos(String area, String tipoFondo) {
        // TODO: Agrupar por rubro o categoría (RF_21)
    }
    public void exportarReporte(String formato) {
        // TODO: Soporte para PDF y Excel (RF_23)
    }
}
