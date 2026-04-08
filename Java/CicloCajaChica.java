import java.util.Date;
class CicloCajaChica{
	private int idCicloCaja;
	private int numeroSemana;
	private Date fechaApertura;
	private Date fechaCierre;
	private double saldoInicial;
	private double totalGastado;
	private EstadoCicloCaja estado;
	
	public CicloCajaChica(int idCicloCaja,int numeroSemana,Date fechaApertura,Date fechaCierre, double saldoInicial,
	double totalGastado, EstadoCicloCaja estado){
		this.idCicloCaja=idCicloCaja;
		this.numeroSemana=numeroSemana;
		this.fechaApertura=fechaApertura;
		this.fechaCierre=fechaCierre;
		this.saldoInicial=saldoInicial;
		this.totalGastado=totalGastado;
		this.estado=estado;
	}
	
	public int getIdCicloCaja() {
		return idCicloCaja;
	}

	public void setIdCicloCaja(int idCicloCaja) {
		this.idCicloCaja = idCicloCaja;
	}

	public int getNumeroSemana() {
		return numeroSemana;
	}

	public void setNumeroSemana(int numeroSemana) {
		this.numeroSemana = numeroSemana;
	}

	public Date getFechaApertura() {
		return fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public double getSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoInicial(double saldoInicial) {
		this.saldoInicial = saldoInicial;
	}

	public double getTotalGastado() {
		return totalGastado;
	}

	public void setTotalGastado(double totalGastado) {
		this.totalGastado = totalGastado;
	}

	public EstadoCicloCaja getEstado() {
		return estado;
	}

	public void setEstado(EstadoCicloCaja estado) {
		this.estado = estado;
	}
}