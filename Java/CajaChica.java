import java.util.List;
import java.util.ArrayList;

class CajaChica extends Fondo{
	private double montoTecho;

	private List<CicloCajaChica> ciclos;//get y set e inic


	
	public CajaChica(double montoTecho){
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