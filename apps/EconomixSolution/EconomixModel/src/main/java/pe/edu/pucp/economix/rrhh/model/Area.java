package pe.edu.pucp.economix.rrhh.model;

import java.util.List;

import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;

public class Area{
    private int idArea;
    private String nombre;
    private String descripcion;
    private boolean estaActivo;
    //Relaciones
    private Empleado jefe;
    private List<CuentaBancaria> cuentasBancarias;
    private List<Rol> roles;

    //Constructores
    public Area(){}
    public Area(int idArea, String nombre, String descripcion, Empleado jefe, List<CuentaBancaria> cuentasBancarias, List<Rol> roles) {
        this.idArea = idArea;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.jefe = jefe;
        this.cuentasBancarias = cuentasBancarias;
        this.roles = roles;
    }
    public Area(String nombre, String descripcion, Empleado jefe, List<CuentaBancaria> cuentasBancarias, List<Rol> roles) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.jefe = jefe;
        this.cuentasBancarias = cuentasBancarias;
        this.roles = roles;
    }

    //Selectores
    public int getIdArea() {
        return idArea;
    }
    public void setIdArea(int idArea) {
        this.idArea = idArea;
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
    public Empleado getJefe() {
        return jefe;
    }
    public void setJefe(Empleado jefe) {
        this.jefe = jefe;
    }
    public boolean isEstaActivo() {
        return estaActivo;
    }
    public void setEstaActivo(boolean estaActivo) {
        this.estaActivo = estaActivo;
    }
    public List<CuentaBancaria> getCuentasBancarias() {
        return cuentasBancarias;
    }
    public void setCuentasBancarias(List<CuentaBancaria> cuentasBancarias) {
        this.cuentasBancarias = cuentasBancarias;
    }
    public List<Rol> getRoles() {
        return roles;
    }
    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    //Metodos
    @Override
    public String toString() {
        return "Area{" +
                "idArea=" + idArea +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", jefe=" + jefe +
                ", cuentasBancarias=" + cuentasBancarias +
                ", roles=" + roles +
                '}';
    }

    public List<SolicitudGasto> obtenerGastosConsolidados() {
        // TODO: Retornar lista de gastos de todos los empleados del área (RF_22)
        return new java.util.ArrayList<>();
    }

    public double calcularPresupuestoEjecutado() {
        // TODO: Sumar todos los desembolsos confirmados del área (RF_21)
        return 0.0;
    }
}
