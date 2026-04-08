import java.util.Date;
import java.util.List;
import java.util.ArrayList;


class SolicitudGasto{
	private int idSolicitudGasto;
	private Date fechaSolicitud;
	private double montoSolicitado;
	private String motivoSolicitud;
	private EstadoSolicitudGasto estado;
	
	private Empleado solicitante; // hacer set get

	private List<ComprobantePago> comprobantes;// inicializar, set ,get 

	private CicloCajaChica ciclo; // get set 26-1

	public SolicitudGasto(int idSolicitudGasto, Date fechaSolicitud, double montoSolicitado, String motivoSolicitud, 
	EstadoSolicitudGasto estado){
	    this.idSolicitudGasto=idSolicitudGasto;
		this.fechaSolicitud=fechaSolicitud;
		this.montoSolicitado=montoSolicitado;
		this.motivoSolicitud=motivoSolicitud;
		this.estado=estado;
		this.comprobantes=new ArrayList<>();
	}
	
	public int getIdSolicitudGasto() {
		return idSolicitudGasto;
    }

	public void setIdSolicitudGasto(int idSolicitudGasto) {
		this.idSolicitudGasto = idSolicitudGasto;
	}

	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	public double getMontoSolicitado() {
		return montoSolicitado;
	}

	public void setMontoSolicitado(double montoSolicitado) {
		this.montoSolicitado = montoSolicitado;
	}

	public String getMotivoSolicitud() {
		return motivoSolicitud;
	}

	public void setMotivoSolicitud(String motivoSolicitud) {
		this.motivoSolicitud = motivoSolicitud;
	}

	public EstadoSolicitudGasto getEstado() {
		return estado;
	}

	public void setEstado(EstadoSolicitudGasto estado) {
		this.estado = estado;
	}
	
}