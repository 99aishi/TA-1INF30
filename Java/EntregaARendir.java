import java.util.Date;
class EntregaARendir{
	private String motivo;
	private double montoSolicitado;
	private Date fechaSolicitud;
	private Date fechaAperturaEntrega;
	private Date fechaCierreEntrega;
	private EstadoEntregaARendir estado;
	
	public EntregaARendir(String motivo, double montoSolicitado, Date fechaSolicitud, Date fechaAperturaEntrega, Date fechaCierreEntrega, 
	EstadoEntregaARendir estado) {
		this.motivo = motivo;
		this.montoSolicitado = montoSolicitado;
		this.fechaSolicitud = fechaSolicitud;
		this.fechaAperturaEntrega = fechaAperturaEntrega;
		this.fechaCierreEntrega = fechaCierreEntrega;
		this.estado = estado;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public double getMontoSolicitado() {
		return montoSolicitado;
	}

	public void setMontoSolicitado(double montoSolicitado) {
		this.montoSolicitado = montoSolicitado;
	}

	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	public Date getFechaAperturaEntrega() {
		return fechaAperturaEntrega;
	}

	public void setFechaAperturaEntrega(Date fechaAperturaEntrega) {
		this.fechaAperturaEntrega = fechaAperturaEntrega;
	}

	public Date getFechaCierreEntrega() {
		return fechaCierreEntrega;
	}

	public void setFechaCierreEntrega(Date fechaCierreEntrega) {
		this.fechaCierreEntrega = fechaCierreEntrega;
	}

	public EstadoEntregaARendir getEstado() {
		return estado;
	}

	public void setEstado(EstadoEntregaARendir estado) {
		this.estado = estado;
	}
}