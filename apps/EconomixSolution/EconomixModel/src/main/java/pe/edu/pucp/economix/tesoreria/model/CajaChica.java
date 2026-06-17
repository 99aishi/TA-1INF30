package pe.edu.pucp.economix.tesoreria.model;

import java.util.List;

import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.tesoreria.model.Moneda;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;

public class CajaChica extends Fondo{
    private double montoTecho;

    //Relaciones
    private List<CicloCajaChica> ciclos;
    private Area areaAsignada;
    private Moneda moneda;
    private CuentaBancaria cuentaOrigen;
    //Constructores
    public CajaChica(){
    }
    public CajaChica(int idFondo, String nombre, double saldoActual, EstadoFondo estado,
                     double montoTecho, List<CicloCajaChica> ciclos, Area areaAsignada,
                     Moneda moneda, CuentaBancaria cuentaOrigen) {
        super(idFondo, nombre, saldoActual, estado);
        this.montoTecho = montoTecho;
        this.ciclos = ciclos;
        this.areaAsignada = areaAsignada;
        this.moneda = moneda;
        this.cuentaOrigen = cuentaOrigen;
    }
    public CajaChica(String nombre, EstadoFondo estado, double montoTecho,
                     List<CicloCajaChica> ciclos, Area areaAsignada,
                     Moneda moneda, CuentaBancaria cuentaOrigen) {
        super(nombre, estado);
        this.montoTecho = montoTecho;
        this.ciclos = ciclos;
        this.areaAsignada = areaAsignada;
        this.moneda = moneda;
        this.cuentaOrigen = cuentaOrigen;
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
    public Moneda getMoneda() {
        return moneda;
    }
    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }
    public CuentaBancaria getCuentaOrigen() {
        return cuentaOrigen;
    }
    public void setCuentaOrigen(CuentaBancaria cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    //Métodos


    @Override
    public String toString() {
        return super.toString() + " CajaChica{" +
                "montoTecho=" + montoTecho +
                ", ciclos=" + ciclos +
                ", areaAsignada=" + areaAsignada +
                ", moneda=" + moneda +
                ", cuentaOrigen=" + cuentaOrigen +
                '}';
    }

    public double obtenerTotalCicloActivo(){
        if (ciclos == null || ciclos.isEmpty()) return 0;
        CicloCajaChica ultimo = ciclos.get(ciclos.size() - 1);
        return ultimo.getSaldoInicial() - ultimo.getTotalGastado();
    }
    public double obtenerTotalGastoCicloActual(){
        if (ciclos == null || ciclos.isEmpty()) return 0;
        CicloCajaChica ultimo = ciclos.get(ciclos.size() - 1);
        return ultimo.getTotalGastado();
    }
}