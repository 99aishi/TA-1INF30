package pe.edu.pucp.economix.operaciones.model;

import java.util.Date;

import pe.edu.pucp.economix.operaciones.model.enums.MedioPago;
import pe.edu.pucp.economix.operaciones.model.enums.TipoTransaccion;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

public class Transaccion{
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

    //COnstructores
    public Transaccion(){}

    public Transaccion(int idTransaccion, TipoTransaccion
                tipoTransaccion, Date fecha, double monto, String numeroOperacionBancaria,
               MedioPago medioPago, double tipoCambio, CuentaBancaria cuentaOrigen,
               CuentaBancaria cuentaDestino, Moneda moneda) {
        this.idTransaccion = idTransaccion;
        this.tipoTransaccion = tipoTransaccion;
        this.fecha = fecha;
        this.monto = monto;
        this.numeroOperacionBancaria = numeroOperacionBancaria;
        this.medioPago = medioPago;
        this.tipoCambio = tipoCambio;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.moneda = moneda;
    }
    public Transaccion(TipoTransaccion tipo, Date fecha, double monto,
                       String numeroOperacionBancaria, MedioPago medioPago, double tipoCambio,
                       CuentaBancaria origen, CuentaBancaria destino, Moneda moneda){
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
    @Override
    public String toString() {
        return "Transaccion{" +
                "idTransaccion=" + idTransaccion +
                ", tipoTransaccion=" + tipoTransaccion +
                ", fecha=" + fecha +
                ", monto=" + monto +
                ", numeroOperacionBancaria='" + numeroOperacionBancaria + '\'' +
                ", medioPago=" + medioPago +
                ", tipoCambio=" + tipoCambio +
                ", cuentaOrigen=" + cuentaOrigen +
                ", cuentaDestino=" + cuentaDestino +
                ", moneda=" + moneda +
                '}';
    }

}
