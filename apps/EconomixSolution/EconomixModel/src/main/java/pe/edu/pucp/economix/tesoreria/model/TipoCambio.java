package pe.edu.pucp.economix.tesoreria.model;

import java.util.Date;

public class TipoCambio {
    private int idTipoCambio;
    private Moneda monedaOrigen;
    private Moneda monedaDestino;
    private double valor;
    private Date fecha;

    public TipoCambio() {}

    public TipoCambio(int idTipoCambio, Moneda monedaOrigen, Moneda monedaDestino,
                      double valor, Date fecha) {
        this.idTipoCambio = idTipoCambio;
        this.monedaOrigen = monedaOrigen;
        this.monedaDestino = monedaDestino;
        this.valor = valor;
        this.fecha = fecha;
    }

    public TipoCambio(Moneda monedaOrigen, Moneda monedaDestino, double valor, Date fecha) {
        this.monedaOrigen = monedaOrigen;
        this.monedaDestino = monedaDestino;
        this.valor = valor;
        this.fecha = fecha;
    }

    public int getIdTipoCambio() {
        return idTipoCambio;
    }

    public void setIdTipoCambio(int idTipoCambio) {
        this.idTipoCambio = idTipoCambio;
    }

    public Moneda getMonedaOrigen() {
        return monedaOrigen;
    }

    public void setMonedaOrigen(Moneda monedaOrigen) {
        this.monedaOrigen = monedaOrigen;
    }

    public Moneda getMonedaDestino() {
        return monedaDestino;
    }

    public void setMonedaDestino(Moneda monedaDestino) {
        this.monedaDestino = monedaDestino;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "TipoCambio{" +
                "idTipoCambio=" + idTipoCambio +
                ", monedaOrigen=" + monedaOrigen +
                ", monedaDestino=" + monedaDestino +
                ", valor=" + valor +
                ", fecha=" + fecha +
                '}';
    }
}
