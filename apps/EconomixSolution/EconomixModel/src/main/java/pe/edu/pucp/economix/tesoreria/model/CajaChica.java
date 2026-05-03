package pe.edu.pucp.economix.tesoreria.model;

import java.util.List;

import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;

public class CajaChica extends Fondo{
    private double montoTecho;

    //Relaciones
    private List<CicloCajaChica> ciclos;
    private Area areaAsignada;
    //Constructores
    public CajaChica(){
    }
    public CajaChica(int idFondo, String nombre, double saldoActual, EstadoFondo estado,
                     double montoTecho, List<CicloCajaChica> ciclos, Area areaAsignada) {
        super(idFondo, nombre, saldoActual, estado);
        this.montoTecho = montoTecho;
        this.ciclos = ciclos;
        this.areaAsignada = areaAsignada;
    }
    public CajaChica(String nombre, EstadoFondo estado, double montoTecho,
                     List<CicloCajaChica> ciclos, Area areaAsignada) {
        super(nombre, estado);
        this.montoTecho = montoTecho;
        this.ciclos = ciclos;
        this.areaAsignada = areaAsignada;
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


    @Override
    public String toString() {
        return super.toString() + " CajaChica{" +
                "montoTecho=" + montoTecho +
                ", ciclos=" + ciclos +
                ", areaAsignada=" + areaAsignada +
                '}';
    }

    public void configurarCajaChica(double montoTecho, CuentaBancaria origen, Empleado responsable) {
        // TODO: Asignar responsable y límites financieros
    }
}