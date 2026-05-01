package pe.edu.pucp.economix.rrhh.model;

import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Area{
    private int idArea;
    private String nombre;
    private String descripcion;
    //Relaciones
    private Empleado jefe;
    private CajaChica cajaChica;
    private List<CuentaBancaria> cuentasBancarias;

    //Constructores
    public Area(){
    }

    public Area(int idArea, String nombre, String descripcion, Empleado jefe, CajaChica cajaChica, List<CuentaBancaria> cuentasBancarias) {
        this.idArea = idArea;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.jefe = jefe;
        this.cajaChica = cajaChica;
        this.cuentasBancarias = cuentasBancarias;
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
    public CajaChica getCajaChica() {
        return cajaChica;
    }
    public void setCajaChica(CajaChica cajaChica) {
        this.cajaChica = cajaChica;
    }
    public List<CuentaBancaria> getCuentasBancarias() {
        return cuentasBancarias;
    }
    public void setCuentasBancarias(List<CuentaBancaria> cuentasBancarias) {
        this.cuentasBancarias = cuentasBancarias;
    }

    //Metodos
    @Override
    public String toString() {
        return "Area{" +
                "idArea=" + idArea +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", jefe=" + jefe +
                ", cajaChica=" + cajaChica +
                ", cuentasBancarias=" + cuentasBancarias +
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
