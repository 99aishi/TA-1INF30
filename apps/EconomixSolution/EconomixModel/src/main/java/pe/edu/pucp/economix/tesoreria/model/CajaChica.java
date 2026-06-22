package pe.edu.pucp.economix.tesoreria.model;

import java.util.List;

import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

public class CajaChica extends Fondo{
    private double montoTecho;

    //Relaciones
    private List<CicloCajaChica> ciclos;
    private CuentaBancaria cuentaBancaria;
    private Moneda moneda;
    //Constructores
    public CajaChica(){
    }
    public CajaChica(int idFondo, String nombre, double saldoActual, EstadoFondo estado,
                     double montoTecho, List<CicloCajaChica> ciclos, CuentaBancaria cuentaBancaria,
                     Moneda moneda) {
        super(idFondo, nombre, saldoActual, estado);
        this.montoTecho = montoTecho;
        this.ciclos = ciclos;
        this.cuentaBancaria = cuentaBancaria;
        this.moneda = moneda;
    }
    public CajaChica(String nombre, EstadoFondo estado, double montoTecho,
                     List<CicloCajaChica> ciclos, CuentaBancaria cuentaBancaria,
                     Moneda moneda) {
        super(nombre, estado);
        this.montoTecho = montoTecho;
        this.ciclos = ciclos;
        this.cuentaBancaria = cuentaBancaria;
        this.moneda = moneda;
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
    public CuentaBancaria getCuentaBancaria() {
        return cuentaBancaria;
    }
    public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }
    public Moneda getMoneda() {
        return moneda;
    }
    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    //Métodos


    @Override
    public String toString() {
        return super.toString() + " CajaChica{" +
                "montoTecho=" + montoTecho +
                ", ciclos=" + ciclos +
                ", cuentaBancaria=" + cuentaBancaria +
                ", moneda=" + moneda +
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