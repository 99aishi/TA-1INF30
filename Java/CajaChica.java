import java.util.List;
import java.util.ArrayList;
import java.util.Date;

class CajaChica extends Fondo{
	private double montoTecho;

	private List<CicloCajaChica> ciclos;//get y set e inic


	
	public CajaChica(int idFondo, String nombre, double saldoActual, EstadoFondo estado, Date fechaCreacion,double montoTecho){
		super(idFondo,nombre,saldoActual,estado,fechaCreacion);
		this.montoTecho=montoTecho;
		this.ciclos=new ArrayList<>();
	}
	
	public double getMontoTecho(){
		return montoTecho;
	}
	
	public void setMontoTecho(double montoTecho){
		this.montoTecho=montoTecho;
	}
}