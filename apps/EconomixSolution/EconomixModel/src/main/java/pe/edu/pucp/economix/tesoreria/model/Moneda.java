package pe.edu.pucp.economix.tesoreria.model;

import java.util.Date;

public class Moneda{
    private int idMoneda;
    private String codigoISO;
    private String simbolo;


    //Constructores
    public Moneda(){}
    public Moneda(int idMoneda, String codigoISO, String simbolo) {
        this.idMoneda = idMoneda;
        this.codigoISO = codigoISO;
        this.simbolo = simbolo;

    }
    //Selectores
    public int getIdMoneda() {
        return idMoneda;
    }
    public void setIdMoneda(int idMoneda) {
        this.idMoneda = idMoneda;
    }
    public String getCodigoISO() {
        return codigoISO;
    }
    public void setCodigoISO(String codigoISO) {
        this.codigoISO = codigoISO;
    }
    public String getSimbolo() {
        return simbolo;
    }
    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    //Metodos

    @Override
    public String toString() {
        return "Moneda{" +
                "idMoneda=" + idMoneda +
                ", codigoISO='" + codigoISO + '\'' +
                ", simbolo='" + simbolo + '\'' +
                '}';
    }

    public double convertirA(double monto, Moneda monedaDestino, double factorCambio) {
        // TODO: Aplicar el factor de cambio para reportes consolidados (RF_21)
        return 0.0;
    }

    public String formatearMonto(double monto) {
        // TODO: Retornar el monto con el símbolo correspondiente (ej: "S/ 100.00")
        return "";
    }

}