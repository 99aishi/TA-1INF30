import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class CajaChica extends Fondo{
	private double montoTecho;
	private List<CicloCajaChica> ciclos;

	//Constructores
	public CajaChica(String nombre, double saldoActual, EstadoFondo estado, Date fechaCreacion,double montoTecho){
		super(nombre,saldoActual,estado,fechaCreacion);
		this.montoTecho=montoTecho;
		this.ciclos=new ArrayList<>();
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

	//Métodos

}