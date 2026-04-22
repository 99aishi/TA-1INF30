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

    // Constructores
    public Fondo(){

    }
    public Fondo(String nombre, double saldoActual, EstadoFondo estado) {
        this.idFondo = this.correlativoID++;
        this.nombre = nombre;
        this.saldoActual = saldoActual;
        this.estado = estado;
    }
    public Fondo(int id, String nombre, double saldoActual, EstadoFondo estado) {
        this.idFondo = id;
        this.nombre = nombre;
        this.saldoActual = saldoActual;
        this.estado = estado;
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

    @Override
    public String toString() {
        return "ID: " + idFondo +
                " Nombre: " + nombre  +
                " Saldo Actual: " + saldoActual;
    }
}
