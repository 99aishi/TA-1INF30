package pe.edu.pucp.economix.tesoreria.model;

import pe.edu.pucp.economix.operaciones.model.Transaccion;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;

import java.util.List;

public class CuentaBancaria {
    private int idCuenta;
    private String nombreBanco;
    private String numeroBancario;
    private String cci;

    //Relaciones
    private Moneda moneda;
    private Empleado empleadoAdministrador;
    private Area areaAdministradora;
    private List<Transaccion> recepciones;
    private List<Transaccion> transferencias;

    //Constructores
    public CuentaBancaria(){};
    public CuentaBancaria(int idCuenta, String nombreBanco, String numeroBancario, String cci,
                          Moneda moneda, Empleado empleadoAdministrador, Area areaAdministradora,
                          List<Transaccion> recepciones, List<Transaccion> transferencias) {
        this.idCuenta = idCuenta;
        this.nombreBanco = nombreBanco;
        this.numeroBancario = numeroBancario;
        this.cci = cci;
        this.moneda = moneda;
        this.empleadoAdministrador = empleadoAdministrador;
        this.areaAdministradora = areaAdministradora;
        this.recepciones = recepciones;
        this.transferencias = transferencias;
    }

    public CuentaBancaria(String nombreBanco, String numeroBancario, String cci, Moneda moneda,
                          Empleado empleadoAdministrador, Area areaAdministradora,
                          List<Transaccion> recepciones, List<Transaccion> transferencias) {
        this.nombreBanco = nombreBanco;
        this.numeroBancario = numeroBancario;
        this.cci = cci;
        this.moneda = moneda;
        this.empleadoAdministrador = empleadoAdministrador;
        this.areaAdministradora = areaAdministradora;
        this.recepciones = recepciones;
        this.transferencias = transferencias;
    }

    //Selectores
    public int getIdCuenta() {
        return idCuenta;
    }
    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }
    public String getNombreBanco() {
        return nombreBanco;
    }
    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }
    public String getNumeroBancario() {
        return numeroBancario;
    }
    public void setNumeroBancario(String numeroBancario) {
        this.numeroBancario = numeroBancario;
    }
    public String getCci() {
        return cci;
    }
    public void setCci(String cci) {
        this.cci = cci;
    }
    public Moneda getMoneda() {
        return moneda;
    }
    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }
    public Empleado getEmpleadoAdministrador() {
        return empleadoAdministrador;
    }
    public void setEmpleadoAdministrador(Empleado empleadoAdministrador) {
        this.empleadoAdministrador = empleadoAdministrador;
    }
    public Area getAreaAdministradora() {
        return areaAdministradora;
    }
    public void setAreaAdministradora(Area areaAdministradora) {
        this.areaAdministradora = areaAdministradora;
    }
    public List<Transaccion> getRecepciones() {
        return recepciones;
    }
    public void setRecepciones(List<Transaccion> recepciones) {
        this.recepciones = recepciones;
    }
    public List<Transaccion> getTransferencias() {
        return transferencias;
    }
    public void setTransferencias(List<Transaccion> transferencias) {
        this.transferencias = transferencias;
    }


    //Metodos

    @Override
    public String toString() {
        return "CuentaBancaria{" +
                "idCuenta=" + idCuenta +
                ", nombreBanco='" + nombreBanco + '\'' +
                ", numeroBancario='" + numeroBancario + '\'' +
                ", cci='" + cci + '\'' +
                ", moneda=" + moneda +
                ", empleadoAdministrador=" + empleadoAdministrador +
                ", areaAdministradora=" + areaAdministradora +
                ", recepciones=" + recepciones +
                ", transferencias=" + transferencias +
                '}';
    }
}