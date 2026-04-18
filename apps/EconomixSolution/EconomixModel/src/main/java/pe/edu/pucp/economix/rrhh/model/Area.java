package pe.edu.pucp.economix.rrhh.model;

import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;

import java.util.ArrayList;
import java.util.List;

public class Area{
    private static int correlativoID = 1;
    private int idArea;
    private String nombre;
    private String descripcion;
    private List<Empleado> empleados;

    //Constructores
    public Area(String nombre, String descripcion) {

        this.idArea = this.correlativoID++;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.empleados=new ArrayList<>();
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

    //Metodos
    public List<SolicitudGasto> obtenerGastosConsolidados() {
        // TODO: Retornar lista de gastos de todos los empleados del área (RF_22)
        return new java.util.ArrayList<>();
    }

    public double calcularPresupuestoEjecutado() {
        // TODO: Sumar todos los desembolsos confirmados del área (RF_21)
        return 0.0;
    }
}
