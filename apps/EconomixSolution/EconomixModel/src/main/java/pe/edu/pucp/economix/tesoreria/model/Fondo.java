package pe.edu.pucp.economix.tesoreria.model;

import pe.edu.pucp.economix.operaciones.model.enums.TipoTransaccion;

public class Fondo {
    private int idFondo;
    private String nombre;
    private double saldoActual;
    private EstadoFondo estado;

    // Constructores
    public Fondo(){}
    public Fondo(int idFondo, String nombre, double saldoActual, EstadoFondo estado) {
        this.idFondo = idFondo;
        this.nombre = nombre;
        this.saldoActual = saldoActual;
        this.estado = estado;
    }
    public Fondo(String nombre, EstadoFondo estado) {
        this.nombre = nombre;
        this.estado = estado;
    }

    // Selectores
    public int getIdFondo() {
        return idFondo;
    }
    public String getNombre() {
        return nombre;
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
    public void setEstado(EstadoFondo estado) {
        this.estado = estado;
    }
    public double getSaldoActual() {
        return saldoActual;
    }
    public void setSaldoActual(double saldoActual) {
        this.saldoActual = saldoActual;
    }
    //Metodos

    @Override
    public String toString() {
        return "Fondo{" +
                "idFondo=" + idFondo +
                ", nombre='" + nombre + '\'' +
                ", saldoActual=" + saldoActual +
                ", estado=" + estado +
                '}';
    }

    public void actualizarSaldo(double monto, TipoTransaccion tipo) {
        switch (tipo) {
            case DESEMBOLSO:
            case REEMBOLSO_DEFICIT:
                this.saldoActual -= monto;
                break;
            case DEVOLUCION_SOBRANTE:
            case REPOSICION_FONDO:
                this.saldoActual += monto;
                break;
        }
    }

    public boolean validarDisponibilidad(double montoRequerido) {
        return this.saldoActual >= montoRequerido;
    }

    public void vincularCuentaBancaria(CuentaBancaria cuenta) {
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta bancaria no puede ser nula");
        }
    }

}
