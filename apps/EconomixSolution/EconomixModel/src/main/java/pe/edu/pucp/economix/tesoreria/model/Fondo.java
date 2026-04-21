package pe.edu.pucp.economix.tesoreria.model;

import pe.edu.pucp.economix.operaciones.model.TipoTransaccion;
import pe.edu.pucp.economix.rrhh.model.Empleado;

import java.util.Date;

public class Fondo {
    private static int correlativoID = 1;
    private int idFondo;
    private String nombre;
    private double saldoActual;
    private EstadoFondo estado;
    private Date fechaCreacion;
    private CuentaBancaria cuentaBancaria; //crear set y get
    private Moneda moneda; // crear set y get
    private Empleado responsable;
    // Constructores
    public Fondo(String nombre, double saldoActual, EstadoFondo estado, Date fechaCreacion) {
        this.idFondo = this.correlativoID++;
        this.nombre = nombre;
        this.saldoActual = saldoActual;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    // Selectores
    public int getIdFondo() {
        return idFondo;
    }
    public String getNombre() {
        return nombre;
    }
    public double getSaldoActual() {
        return saldoActual;
    }
    public EstadoFondo getEstado() {
        return estado;
    }
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    public CuentaBancaria getCuentaBancaria(){
        return this.cuentaBancaria;
    }
    public Moneda getMoneda(){
        return this.moneda;
    }
    public Empleado getResponsable(){ return this.responsable;}
    public void setIdFondo(int idFondo) {
        this.idFondo = idFondo;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setSaldoActual(double saldoActual) {
        this.saldoActual = saldoActual;
    }
    public void setEstado(EstadoFondo estado) {
        this.estado = estado;
    }
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    public void setCuentaBancaria(CuentaBancaria cuenta){
        this.cuentaBancaria = cuenta;
    }
    public void setMoneda(Moneda moneda){
        this.moneda = moneda;
    }
    public void setResponsable(Empleado responsable){this.responsable=responsable;}

    //Metodos
    public void actualizarSaldo(double monto, TipoTransaccion tipo) {
        // TODO: Lógica para sumar (Reposición) o restar (Desembolso) al saldoActual
    }

    public boolean validarDisponibilidad(double montoRequerido) {
        // TODO: Verificar si el saldoActual es suficiente antes de proceder
        return false;
    }

    public void vincularCuentaBancaria(CuentaBancaria cuenta) {
        // TODO: Asignar la cuenta de origen del fondo (RF_11)
    }
}
