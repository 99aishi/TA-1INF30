package pe.edu.pucp.economix.tesoreria.model;

public class Moneda{
    private int idMoneda;
    private String codigoISO;
    private String simbolo;
    private String nombre;
    private String descripcion;
    private boolean activa;


    //Constructores
    public Moneda(){}
    public Moneda(int idMoneda, String codigoISO, String simbolo,
                  String nombre, String descripcion, Boolean activa) {
        this.idMoneda = idMoneda;
        this.codigoISO = codigoISO;
        this.simbolo = simbolo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activa = activa;
    }
    public Moneda(String codigoISO, String simbolo) {
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
                ", nombre ='" + nombre + '\'' +
                ", descripcion ='" + descripcion + '\'' +
                '}';
    }

    public double convertirA(double monto, Moneda monedaDestino, double factorCambio) {
        return monto * factorCambio;
    }

    public String formatearMonto(double monto) {
        return (simbolo != null ? simbolo : "") + " " + String.format("%.2f", monto);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
