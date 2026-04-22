package pe.edu.pucp.economix.tesoreria.model;

import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;

public class CuentaBancaria {
    private static int correlativoID = 1;
    private int idCuenta;
    private String nombreBanco;
    private String numeroBancario;
    private String cci;
    private Empleado administrador; //set y get , c copia
    private Area areaAdministradora;
    private Moneda moneda;

    //Constructores
    public CuentaBancaria(String nombreBanco, String numeroBancario, String cci, Empleado administrador) {
        this.idCuenta = this.correlativoID++;
        this.nombreBanco = nombreBanco;
        this.numeroBancario = numeroBancario;
        this.cci = cci;
        this.administrador = administrador;
    }
    public CuentaBancaria(){};
    //Selectores
    public int getIdCuenta() {
        return idCuenta;
    }
    public String getNombreBanco() {
        return nombreBanco;
    }
    public String getNumeroBancario() {
        return numeroBancario;
    }
    public String getCci() {
        return cci;
    }
    public Empleado getAdministrador(){
        return this.administrador;
    }
    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }
    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }
    public void setNumeroBancario(String numeroBancario) {
        this.numeroBancario = numeroBancario;
    }
    public void setCci(String cci) {
        this.cci = cci;
    }
    public void setAdministrador(Empleado admin){
        this.administrador = admin;
    }
    public Moneda getMoneda() {return new Moneda(moneda);}
    public void setMoneda(Moneda m){this.moneda=new Moneda(m);}
    public Area getAreaAdministradora() {
        return areaAdministradora;
    }
    public void setAreaAdministradora(Area areaAdministradora) {
        this.areaAdministradora = areaAdministradora;
    }

    //Metodos
    public String toString() {
        return "ID: " + this.idCuenta + " - NUMERO BANCARIO: " + this.numeroBancario + " - BANCO: " + this.nombreBanco +
                " - DUEÑO: " + this.administrador.getNombre() + " " + this.administrador.getApellidoPaterno() + " | Area " +
                this.areaAdministradora.getNombre();
    }
}