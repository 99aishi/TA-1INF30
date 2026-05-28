package pe.edu.pucp.economix.tesoreria.model;

import pe.edu.pucp.economix.operaciones.model.enums.TipoTransaccion;

public class Fondo {
    private int idFondo;
    private String nombre;
    private EstadoFondo estado;

    // Constructores
    public Fondo(){}
    public Fondo(int idFondo, String nombre, double saldoActual, EstadoFondo estado) {
        this.idFondo = idFondo;
        this.nombre = nombre;
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
    //Metodos

    @Override
    public String toString() {
        return "Fondo{" +
                "idFondo=" + idFondo +
                ", nombre='" + nombre + '\'' +
                ", estado=" + estado +
                '}';
    }

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
