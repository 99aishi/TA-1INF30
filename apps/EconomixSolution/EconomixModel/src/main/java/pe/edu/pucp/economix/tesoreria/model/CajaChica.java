package pe.edu.pucp.economix.tesoreria.model;

import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CajaChica extends Fondo{
    private double montoTecho;
    private List<CicloCajaChica> ciclos;
    private Area areaAsignada;
    //Constructores
    public CajaChica(){

    }
    public CajaChica(int id_Fondo ,String nombre, double saldoActual, EstadoFondo estado, double montoTecho,Area areaAsignada){
        super(id_Fondo,nombre,saldoActual,estado);
        this.montoTecho=montoTecho;
        this.ciclos=new ArrayList<>();
        this.areaAsignada=areaAsignada;
    }
    public CajaChica(String nombre, double saldoActual, EstadoFondo estado, double montoTecho,
                     Area areaAsignada){

        super(nombre,saldoActual,estado);
        this.montoTecho=montoTecho;
        this.ciclos=new ArrayList<>();
        this.areaAsignada=areaAsignada;
    }
    //Selectores
    public double getMontoTecho(){
        return montoTecho;
    }
    public void setMontoTecho(double montoTecho){
        this.montoTecho=montoTecho;
    }
    public CicloCajaChica getcCicloCajaChica(int n){
        return ciclos.get(n);
    }
    public void setCicloCajaChica(CicloCajaChica ciclo){
        this.ciclos.add(ciclo);
    }

    public Area getAreaAsignada() {
        return areaAsignada;
    }
    public void setAreaAsignada(Area area) {
        this.areaAsignada = area;
    }

    //Métodos
    public void configurarCajaChica(double montoTecho, CuentaBancaria origen, Empleado responsable) {
        // TODO: Asignar responsable y límites financieros (RF_11)
    }

    @Override
    public String toString() {
        return "Caja Chica: " + super.toString() +
                "montoTecho: " + montoTecho +
                " ID Area Asignada: " + areaAsignada.getIdArea();
    }
}